package games.snake;

import games.MultiplayerGame;
import games.snake.dtos.SnakeSightRaysDTO;
import games.snake.savegame.SaveGameUtil;
import games.snake.savegame.SavedGameDTO;
import neat.phenotype.NeuralNetwork;
import utils.Direction;
import utils.Settings;
import visualizations.snakeGraphic.explanations.IntroScreenLetters;

import java.util.ArrayList;
import java.util.List;

import static utils.Util.arrayCopy;
import static utils.Util.randomFreeCoordinate;
import static utils.Util.repeat;

public class SnakeGameIntro implements MultiplayerGame {
    private final int columns;
    private final int rows;
    protected int[][] grid;
    protected List<Snake> snakes;
    private SnakeSightRaysDTO snakeSightRaysDTO;
    public int numOfFood;
    private IntroScreenLetters introScreenLetters;
    private int introSignAppear = 150; // 20 for testing

    public SnakeGameIntro() {
        // TODO set wide
        this.rows = Settings.GRID_ROW_PIXELS / Settings.PIXELS_PER_SQUARE;
        this.columns = Settings.GRID_COLUMN_PIXELS / Settings.PIXELS_PER_SQUARE;
        this.snakes = new ArrayList<>();
        reset();
        this.introScreenLetters = new IntroScreenLetters();
    }

    @Override
    public int[] play(List<NeuralNetwork> neuralNetworks) {
        for (int i = 0; i < snakes.size(); i++)
            snakes.get(i).neuralNetwork = neuralNetworks.get(i);

        for (int move = 0; move < Settings.MAX_NUM_OF_MOVES; move++) {
            for (Snake snake : snakes) {
                var networkOutput = snake.neuralNetwork.getNetworkOutput(snakeSightRaysDTO.getInput(snake));
                moveSnakeToDirection(snake, outputToDirection(networkOutput));
            }
        }

        return snakes.stream()
                .mapToInt(snake -> snake.snakeScore)
                .toArray();
    }

    @Override
    public void reset() {
        initGrid();
        initSnakes();
        placeSnakes(0);
        snakeSightRaysDTO = new SnakeSightRaysDTO(grid);
        numOfFood = 0;
        repeat.accept(Settings.MAX_NUM_OF_FOOD, this::placeFood);
    }

    private void initGrid() {
        this.grid = new int[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (Settings.HAS_WALL && (row == 0 || row == rows - 1 || column == 0 || column == columns - 1))
                    grid[row][column] = SnakeMap.WALL.value;
            }
        }
    }

    private void initSnakes() {
        snakes = new ArrayList<>();
        for (int i = 0; i < Settings.NUM_OF_PLAYERS; i++) {
            var coordinates = randomFreeCoordinate(grid);
            var snake = new Snake(grid, coordinates.getFirst(), coordinates.getSecond(), Direction.NONE, i);
            snakes.add(snake);
        }
    }

    private void placeSnakes(int numOfPlaced) {
        numOfPlaced = Math.min(numOfPlaced, Settings.NUM_OF_PLAYERS);
//        for (int i = 0; i < numOfPlaced; i++) {
//                var snake = snakes.get(i);
//                snake.placeSnake();
//        }
        var snake = snakes.get(numOfPlaced);
        snake.placeSnake();
    }

    /**
     * If there is less food on the grid then Settings.maxNumberOfFood one additional food will be added on random grid
     * square.
     */
    private void placeFood() {
        if (numOfFood < Settings.MAX_NUM_OF_FOOD) {
            var coordinates = randomFreeCoordinate(grid);
            grid[coordinates.getFirst()][coordinates.getSecond()] = SnakeMap.FOOD.value;
            numOfFood++;
        }
    }

    /**
     * Moves Games.Snake to given direction
     *
     * @param direction where to move snake
     */
    protected void moveSnakeToDirection(Snake snake, Direction direction) {
        if (Settings.SELF_COLLISION) {
            // If SELF_COLLISION is enabled than snake that is longer than one body part cannot go opposite to its last direction because it would hit its tail
            if (snake.size() > 1 && direction == Direction.opposite(snake.lastDirection) || direction == Direction.NONE)
                direction = snake.lastDirection;
        } else {
            snake.lastDirection = direction;
        }

        int headRow = snake.bodyParts.get(0).row;
        int headColumn = snake.bodyParts.get(0).column;

        switch (direction) {
            case UP -> moveSnake(snake, headRow - 1, headColumn);
            case DOWN -> moveSnake(snake, headRow + 1, headColumn);
            case LEFT -> moveSnake(snake, headRow, headColumn - 1);
            case RIGHT -> moveSnake(snake, headRow, headColumn + 1);
        }
    }

    protected int wrapAroundCoordinates(int coordinate, int max) {
        if (coordinate == -1)
            return max - 1;
        if (coordinate == max)
            return 0;
        return coordinate;
    }

    /**
     * Moves snake if there is collision with other snake or wall, snake will be reset.
     *
     * @param snake  to move
     * @param row    where to move
     * @param column where to move
     */
    protected void moveSnake(Snake snake, int row, int column) {
        if (!Settings.HAS_WALL) {
            row = wrapAroundCoordinates(row, rows);
            column = wrapAroundCoordinates(column, columns);
        }
        if (snakeCollision(snake, row, column)) {
            var coordinates = randomFreeCoordinate(grid);
            var foodPlaced = snake.removeSnake(Settings.LEAVE_CORPSE);
            numOfFood += foodPlaced;
            snake.resetSnake(coordinates.getFirst(), coordinates.getSecond(), Direction.randomDirection(), 3);
            snake.snakeScore += Settings.DEATH_PENALTY;
            snake.placeSnake();
        } else {
            moveSnakeByOne(snake, row, column);
            if (snake.stepsMoved == Settings.STEPS_TO_REDUCTION) {
                snake.stepsMoved = 0;
                snake.reduceSnakeByOne();
            }
        }
    }

    /**
     * Moves snake to given row and column. If there is food snake length will be increased by one and new food will
     * be placed onto grid.
     *
     * @param snake  to move
     * @param row    where to move
     * @param column where to move
     */
    protected void moveSnakeByOne(Snake snake, int row, int column) {
        var bodyParts = snake.bodyParts;

        bodyParts.get(0).isHead = false;
        bodyParts.add(0, new BodyPart(true, row, column));

        if (grid[row][column] == SnakeMap.FOOD.value) {
            snake.placeSnake();
            numOfFood--;
            placeFood();
            snake.snakeScore += 1;
        } else {
            snake.removeSnake(false);
            bodyParts.remove(bodyParts.size() - 1);
            snake.placeSnake();
        }

        snake.stepsMoved++;
    }

    /**
     * Checks whether snake can move specific grid coordinates.
     *
     * @return true moving to coordinates will result in death
     */
    protected boolean snakeCollision(Snake snake, int row, int column) {
        return grid[row][column] == SnakeMap.WALL.value || snake.isSnakeCollision(row, column);
    }

    /**
     * The function converts output of {@link NeuralNetwork} to direction where to move.
     *
     * @param neuralNetworkOutput output of {@link NeuralNetwork}
     * @return direction where {@link NeuralNetwork decided to move
     */
    protected Direction outputToDirection(double[] neuralNetworkOutput) {
        return switch (maxValueIndex(neuralNetworkOutput)) {
            case 0 -> Direction.UP;
            case 1 -> Direction.RIGHT;
            case 2 -> Direction.DOWN;
            case 3 -> Direction.LEFT;
            default -> Direction.NONE;
        };
    }

    /**
     * The function returns index of max value in given array
     *
     * @param array double array
     * @return index of max number in array
     */
    protected int maxValueIndex(double[] array) {
        int maxIndex = 0;
        double max = array[0];

        for (int i = 0; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * Plays the game and saves grid arrangements so they can be used later e.g. for visualization.
     */
    @Override
    public SavedGameDTO saveSnakeMoves(List<NeuralNetwork> neuralNetworks) {
        var savedGameDTO = new SavedGameDTO();
        int frameCount = 0;
        for (int move = 0; move < 300; move++) {
            if (move == introSignAppear) {
                clearAllFood();
                numOfFood += introScreenLetters.signInsert(grid);
            }
            frameCount++;
            if (move >= introSignAppear) {
                placeSnakes(calculateSnakeWhichIsPlaced(move));
                for (int i = 0; i < calculateSnakeWhichIsPlaced(move) + 1; i++) {
                    var networkOutput = neuralNetworks.get(i).getNetworkOutput(snakeSightRaysDTO.getInput(snakes.get(i)));
                    moveSnakeToDirection(snakes.get(i), outputToDirection(networkOutput));
                }
            } else {
                var networkOutput = neuralNetworks.get(0).getNetworkOutput(snakeSightRaysDTO.getInput(snakes.get(0)));
                moveSnakeToDirection(snakes.get(0), outputToDirection(networkOutput));
            }

            savedGameDTO.grid.add(arrayCopy(grid));
        }
        setSaveGameMetadata(savedGameDTO);
        savedGameDTO.totalFrames = frameCount;
        return savedGameDTO;
    }

    public int calculateSnakeWhichIsPlaced(int move) {
        int snakeNumber = (move - introSignAppear) / 10;
        return Math.min(snakeNumber, 9);
    }

    public void setSaveGameMetadata(SavedGameDTO savedGameDTO) {
        savedGameDTO.rows = rows;
        savedGameDTO.columns = columns;
        savedGameDTO.fileName = SaveGameUtil.getCurrentDateTimeAsString() + ".sav";
    }

    public void clearAllFood() {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (grid[row][column] == SnakeMap.FOOD.value) {
                    grid[row][column] = SnakeMap.EMPTY.value;
                }
            }
        }
        numOfFood = 0;
    }

    /**
     * Prints snakeGame using ascii characters.
     */
    public void printSnakeGame() {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (grid[row][column] >= SnakeMap.BODY.value)
                    System.out.print(grid[row][column]);
                else
                    System.out.print(" " + grid[row][column] + " ");
            }
            System.out.println();
        }
    }
}
