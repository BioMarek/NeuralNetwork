package games.snake;

import games.MultiplayerGame;
import games.snake.dtos.SnakeSightDTO;
import games.snake.savegame.SavedGameDTO;
import interfaces.NeuralNetwork;
import utils.Direction;
import utils.Pair;
import utils.Util;

import java.util.ArrayList;
import java.util.List;

import static games.snake.SnakeMap.BODY;
import static games.snake.SnakeMap.HEAD;
import static utils.Settings.DEATH_PENALTY;
import static utils.Settings.GRID_ROWS;
import static utils.Settings.GRID_COLUMNS;
import static utils.Settings.HAS_WALL;
import static utils.Settings.LEAVE_CORPSE;
import static utils.Settings.MAX_NUM_OF_FOOD;
import static utils.Settings.NUM_OF_PLAYERS;
import static utils.Settings.PIXELS_PER_SQUARE;
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
        this.rows = GRID_ROWS / PIXELS_PER_SQUARE;
        this.columns = GRID_COLUMNS / PIXELS_PER_SQUARE;
        reset();
    }

    @Override
    public int[] play(List<NeuralNetwork> neuralNetworks, int maxNumberOfMoves) {
        for (int move = 0; move < maxNumberOfMoves; move++) {
            for (int i = 0; i < neuralNetworks.size(); i++) {
                var networkOutput = neuralNetworks.get(i).getNetworkOutput(snakeSightDTO.getInput_8(snakes.get(i)));
                moveSnakeToDirection(snakes.get(i), outputToDirection(networkOutput));
            }
        }

        int[] result = new int[neuralNetworks.size()];
        for (int i = 0; i < snakes.size(); i++) {
            result[i] = snakes.get(i).snakeScore;
        }
        return result;
    }

    @Override
    public void reset() {
        initGrid();
        initSnakes();
        snakeSightDTO = new SnakeSightDTO(grid);
        numOfFood = 0;
        repeat.accept(MAX_NUM_OF_FOOD, this::placeFood);
    }

    private void initGrid() {
        this.grid = new int[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (HAS_WALL && (row == 0 || row == rows - 1 || column == 0 || column == columns - 1))
                    grid[row][column] = SnakeMap.WALL.value;
            }
        }
    }

    private void initSnakes() {
        snakes = new ArrayList<>();
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            var coordinates = randomFreeCoordinate(grid);
            var snake = new Snake(coordinates.getFirst(), coordinates.getSecond(), Direction.randomDirection(), i);
            placeSnake(snake);
            snakes.add(snake);
        }
    }

    protected void placeSnake(Snake snake) {
        for (int j = snake.bodyParts.size() - 1; j >= 0; j--) { // head will be always on top of other bodyparts
            var bodyPart = snake.bodyParts.get(j);
            if (bodyPart.isHead)
                grid[bodyPart.row][bodyPart.column] = snake.name + HEAD.value;
            else
                grid[bodyPart.row][bodyPart.column] = snake.name + BODY.value;
        }
    }

    /**
     * Removes {@link Snake} from grid. Grid squares that were occupied by snake {@link BodyPart}s will get new number
     * based on whether we want to leave food in place of dead snake or just remove it.
     *
     * @param snake    to remove
     * @param snakeMap value to place on grid squares where snake bodyparts were
     */
    public void removeSnake(Snake snake, SnakeMap snakeMap) {
        for (BodyPart bodyPart : snake.bodyParts) {
            grid[bodyPart.row][bodyPart.column] = snakeMap.value;
        }
    }

    /**
     * If there is less food on the grid then Settings.maxNumberOfFood one additional food will be added on random grid
     * square.
     */
    private void placeFood() {
        if (numOfFood < MAX_NUM_OF_FOOD) {
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
            if (LEAVE_CORPSE) {
                numOfFood += snake.uniqueTilesOccupied();
                removeSnake(snake, SnakeMap.FOOD);
            } else
                removeSnake(snake, SnakeMap.EMPTY);
            snake.resetSnake(coordinates.getFirst(), coordinates.getSecond(), Direction.randomDirection());
            snake.snakeScore += DEATH_PENALTY;
            placeSnake(snake);
        } else {
            moveSnakeByOne(snake, row, column);
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
            placeSnake(snake);
            numOfFood--;
            placeFood();
            snake.snakeScore += 1;
        } else {
            removeSnake(snake, SnakeMap.EMPTY);
            bodyParts.remove(bodyParts.size() - 1);
            placeSnake(snake);
        }
    }

    /**
     * Checks whether snake can move specific grid coordinates.
     *
     * @return true moving to coordinates will result in death
     */
    protected boolean snakeCollision(Snake snake, int row, int column) {
        return grid[row][column] == SnakeMap.WALL.value || (grid[row][column] != snake.name + BODY.value && grid[row][column] >= BODY.value);
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
            savedGameDTO.grid.add(arrayCopy(grid));
        }
        return savedGameDTO;
    }

    public static Pair<Integer> randomFreeCoordinate(int[][] grid) {
        while (true) {
            int row = Util.randomInt(1, grid.length - 1);
            int column = Util.randomInt(1, grid[0].length - 1);
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
                if (grid[row][column] >= BODY.value)
                    System.out.print(grid[row][column]);
                else
                    System.out.print(" " + grid[row][column] + " ");
            }
            System.out.println();
        }
    }
}
