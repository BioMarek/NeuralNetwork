package games.freeEvolution;

import games.snake.BodyPart;
import games.snake.SnakeMap;
import games.snake.dtos.SnakeSightDTO;
import games.snake.savegame.SaveGameUtil;
import games.snake.savegame.SavedGameDTO;
import neat.phenotype.NeuralNetwork;
import utils.Direction;
import utils.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Util.arrayCopy;
import static utils.Util.randomFreeCoordinate;
import static utils.Util.repeat;

public class FEGame {
    private final int columns;
    private final int rows;
    public final int inputs;
    public final int outputs;
    protected int[][] grid;
    protected List<FESnake> snakes;
    private SnakeSightDTO snakeSightDTO;
    public int numOfFood;
    public Map<Integer, Integer> scores;


    public FEGame(int inputs, int outputs) {
        this.rows = Settings.GRID_ROW_PIXELS / Settings.PIXELS_PER_SQUARE;
        this.columns = Settings.GRID_COLUMN_PIXELS / Settings.PIXELS_PER_SQUARE;
        this.inputs = inputs;
        this.outputs = outputs;
        // TODO scores should be printed or used or removed
        this.scores = new HashMap<>();
        reset();
    }

    public void reset() {
        initGrid();
        initSnakes();
        snakeSightDTO = new SnakeSightDTO(grid);
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
            createSnake();
        }
    }

    protected void createSnake() {
        var snake = new FESnake(grid);
        snake.genotype = new FEGenotype(inputs, outputs);
        snake.placeSnake();
        snakes.add(snake);
        scores.put(snake.id, 1);
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
    protected void moveSnakeToDirection(FESnake snake, Direction direction) {
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
    protected void moveSnake(FESnake snake, int row, int column) {
        if (!Settings.HAS_WALL) {
            row = wrapAroundCoordinates(row, rows);
            column = wrapAroundCoordinates(column, columns);
        }
        if (snakeCollision(snake, row, column)) {
            var foodPlaced = snake.removeSnake(Settings.LEAVE_CORPSE);
            numOfFood += foodPlaced;
            snakes.remove(snake);
        } else {
            moveSnakeByOne(snake, row, column);
            if (snake.stepsMoved == Settings.STEPS_TO_REDUCTION) {
                snake.stepsMoved = 0;
                snake.reduceSnakeByOne();
                if (snake.size() == 0) {
                    snakes.remove(snake);
                }
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
    protected void moveSnakeByOne(FESnake snake, int row, int column) {
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
    protected boolean snakeCollision(FESnake snake, int row, int column) {
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
    public SavedGameDTO saveSnakeMoves() {
        var savedGameDTO = new SavedGameDTO();
        int frameCount = 0;

        for (int move = 0; move < Settings.MAX_NUM_OF_MOVES; move++) {
            frameCount++;
            for (int i = 0; i < snakes.size(); i++) {
                var networkOutput = snakes.get(i).getNeuralNetwork().getNetworkOutput(snakeSightDTO.getInput(snakes.get(i)));
                moveSnakeToDirection(snakes.get(i), outputToDirection(networkOutput));
            }
            if (snakes.size() < Settings.NUM_OF_PLAYERS) {
                for (int i = 0; i < Settings.NUM_OF_PLAYERS - snakes.size(); i++) {
                    createSnake();
                }
            }
            savedGameDTO.grid.add(arrayCopy(grid));
            for (int i = 0; i < snakes.size(); i++) {
                if (snakes.get(i).size() >= Settings.OFFSPRING_THRESHOLD) {
                    var offspring = snakes.get(i).produceOffSpring();
                    snakes.add(offspring);
                    var currentValue = scores.get(offspring.id);
                    scores.put(offspring.color, currentValue + 1);
                    offspring.placeSnake();
                }
            }
            if (snakes.size() == 0)
                break;
        }
        setSaveGameMetadata(savedGameDTO);
        savedGameDTO.totalFrames = frameCount;
        return savedGameDTO;
    }

    public void setSaveGameMetadata(SavedGameDTO savedGameDTO) {
        savedGameDTO.rows = rows;
        savedGameDTO.columns = columns;
        savedGameDTO.fileName = SaveGameUtil.getCurrentDateTimeAsString() + ".sav";

    }

    public void calculateScores(){

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
