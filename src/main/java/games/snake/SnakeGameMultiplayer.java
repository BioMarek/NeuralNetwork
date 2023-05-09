package games.snake;

import games.MultiplayerGame;
import games.snake.dtos.SnakeSightDTO;
import games.snake.savegame.SavedGameDTO;
import neat.phenotype.NeuralNetwork;
import utils.Direction;
import utils.Pair;
import utils.Settings;
import utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.Util.arrayCopy;
import static utils.Util.repeat;

public class SnakeGameMultiplayer implements MultiplayerGame {
    private final int columns;
    private final int rows;
    protected int[][] grid;
    protected List<Snake> snakes;
    private SnakeSightDTO snakeSightDTO;
    public int numOfFood;

    public SnakeGameMultiplayer() {
        this.rows = Settings.GRID_ROWS / Settings.PIXELS_PER_SQUARE;
        this.columns = Settings.GRID_COLUMNS / Settings.PIXELS_PER_SQUARE;
        reset();
    }

    @Override
    public int[] play(List<NeuralNetwork> neuralNetworks, int maxNumberOfMoves) {
        for (int i = 0; i < snakes.size(); i++)
            snakes.get(i).neuralNetwork = neuralNetworks.get(i);

        for (int move = 0; move < maxNumberOfMoves; move++) {
            for (Snake snake : snakes) {
                var networkOutput = snake.neuralNetwork.getNetworkOutput(snakeSightDTO.getInput_8(snake));
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
            var coordinates = randomFreeCoordinate(grid);
            var snake = new Snake(grid, coordinates.getFirst(), coordinates.getSecond(), Direction.randomDirection(), i);
            snake.placeSnake();
            snakes.add(snake);
        }
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
        direction = direction == Direction.NONE ? snake.lastDirection : direction;
        snake.lastDirection = direction;
        int headRow = snake.bodyParts.get(0).row;
        int headColumn = snake.bodyParts.get(0).column;

        switch (direction) {
            case UP -> moveSnake(snake, headRow - 1, headColumn);
            case DOWN -> moveSnake(snake, headRow + 1, headColumn);
            case LEFT -> moveSnake(snake, headRow, headColumn - 1);
            case RIGHT -> moveSnake(snake, headRow, headColumn + 1);
        }
    }

    /**
     * Moves snake if there is collision with other snake or wall, snake will be reset.
     *
     * @param snake  to move
     * @param row    where to move
     * @param column where to move
     */
    protected void moveSnake(Snake snake, int row, int column) {
        if (snakeCollision(snake, row, column)) {
            var coordinates = randomFreeCoordinate(grid);
            snake.removeSnake(Settings.LEAVE_CORPSE);
            snake.resetSnake(coordinates.getFirst(), coordinates.getSecond(), Direction.randomDirection());
            snake.snakeScore += Settings.DEATH_PENALTY;
            snake.placeSnake();
        } else {
            moveSnakeByOne(snake, row, column);
            if (snake.stepsMoved == Settings.STEPS_TO_REDUCTION) {
                snake.stepsMoved = 0;
                snake.reduceSnakeByOne(SnakeMap.EMPTY);
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
        return grid[row][column] == SnakeMap.WALL.value || (grid[row][column] != snake.name + SnakeMap.BODY.value && grid[row][column] >= SnakeMap.BODY.value);
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
    public SavedGameDTO saveSnakeMoves(List<NeuralNetwork> neuralNetworks, int maxNumberOfMoves) {
        var savedGameDTO = new SavedGameDTO();
        savedGameDTO.rows = rows;
        savedGameDTO.columns = columns;
        for (int move = 0; move < maxNumberOfMoves; move++) {
            for (int i = 0; i < neuralNetworks.size(); i++) {
                var networkOutput = neuralNetworks.get(i).getNetworkOutput(snakeSightDTO.getInput_8(snakes.get(i)));
                moveSnakeToDirection(snakes.get(i), outputToDirection(networkOutput));
            }
            int[] scores = new int[snakes.size()];
            for (int i = 0; i < neuralNetworks.size(); i++) {
                scores[i] = snakes.get(i).snakeScore;
            }
            savedGameDTO.scores.add(scores);
            savedGameDTO.grid.add(arrayCopy(grid));
        }
        return savedGameDTO;
    }

    public static Pair<Integer> randomFreeCoordinate(int[][] grid) {
        int row = Util.randomInt(1, grid.length - 1);
        int column = Util.randomInt(1, grid[0].length - 1);
        while (true) {
            if (grid[row][column] != SnakeMap.EMPTY.value) {
                row = Util.randomInt(1, grid.length - 1);
                column = Util.randomInt(1, grid.length - 1);
            } else {
                return new Pair<>(row, column);
            }
        }
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
