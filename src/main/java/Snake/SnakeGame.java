package Snake;

import Interfaces.Game;
import NeuralNetwork.NeuralNetwork;
import NeuralNetwork.Util;

import java.util.*;

/**
 * Snake game it used check whether {@link NeuralNetwork} can play simple game.
 */
public class SnakeGame implements Game {
    private final int EMPTY = 0;
    private final int WALL = 1;
    private final int BODY = 2;
    private final int HEAD = 3;
    private final int FOOD = 4;
    private Map<Integer, String> tilesMap;
    private Map<String, Direction> keyToDirection;
    private Map<Integer, Direction> indexToDirection;

    private final int size;
    private int[][] grid;

    protected List<BodyPart> snake;
    protected int foodRow;
    protected int foodColumn;

    public Direction lastDirection;
    public int snakeScore;
    public boolean isGameOver = false;

    public SnakeGame(int size) {
        this.size = size;
        initMaps();
        reset();
    }

    /**
     * Resets game into initial state.
     */
    public void reset() {
        initSnake();
        placeFood();
        snakeToGrid();
    }

    protected void initMaps() {
        tilesMap = new HashMap<>();
        tilesMap.put(WALL, "X");
        tilesMap.put(HEAD, "H");
        tilesMap.put(BODY, "B");
        tilesMap.put(FOOD, "O");
        tilesMap.put(EMPTY, " ");

        keyToDirection = new HashMap<>();
        keyToDirection.put("w", Direction.UP);
        keyToDirection.put("s", Direction.DOWN);
        keyToDirection.put("a", Direction.LEFT);
        keyToDirection.put("d", Direction.RIGHT);

        indexToDirection = new HashMap<>();
        indexToDirection.put(0, Direction.UP);
        indexToDirection.put(1, Direction.DOWN);
        indexToDirection.put(2, Direction.LEFT);
        indexToDirection.put(3, Direction.RIGHT);
    }

    /**
     * Creates initial snake game setup
     */
    protected void initSnake() {
        snakeScore = 0;
        lastDirection = Direction.UP;
        this.grid = new int[size][size];

        snake = new ArrayList<>();
        snake.add(new BodyPart(true, size / 2, size / 2));
        snake.add(new BodyPart(false, size / 2 + 1, size / 2));
        snake.add(new BodyPart(false, size / 2 + 2, size / 2));

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (row == 0 || row == size - 1 || column == 0 || column == size - 1)
                    grid[row][column] = WALL;
            }
        }
    }

    /**
     * Tries to place food on grid randomly. If the food is placed on snake new random position is found.
     */
    protected void placeFood() {
        while (true) {
            int row = Util.randomInt(1, size - 1);
            int column = Util.randomInt(1, size - 1);
            if (grid[row][column] != EMPTY) {
                row = Util.randomInt(1, size - 1);
                column = Util.randomInt(1, size - 1);
            } else {
                foodRow = row;
                foodColumn = column;
                grid[row][column] = FOOD;
                return;
            }
        }
    }

    /**
     * Places snake into grid.
     */
    protected void snakeToGrid() {
        snake.forEach((bodyPart) -> grid[bodyPart.row][bodyPart.column] = bodyPart.isHead ? HEAD : BODY);
    }

    /**
     * Prints snakeGame using asci characters.
     */
    public void printSnakeGame() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                System.out.print(tilesMap.get(grid[row][column]));
            }
            System.out.println();
        }
    }

    /**
     * Main loop when game is played by person.
     */
    public void mainLoop() {
        Scanner scanner = new Scanner(System.in);
        while (!isGameOver) {
            String move = scanner.next();
            moveSnake(toDirection(move));
            snakeToGrid();
            printSnakeGame();
        }
    }

    /**
     * Maps pressed key to {@link Direction}.
     *
     * @param key String representation of key
     * @return {@link Direction} corresponding to pressed key
     */
    protected Direction toDirection(String key) {
        Direction directionToMove = keyToDirection.get(key);
        lastDirection = directionToMove == null || lastDirection == Direction.opposite(directionToMove) ? lastDirection : directionToMove;
        return lastDirection;
    }

    /**
     * Moves Snake to given direction
     *
     * @param direction where to move snake
     */
    protected void moveSnake(Direction direction) {
        int headRow = snake.get(0).row;
        int headColumn = snake.get(0).column;
        switch (direction) {
            case UP:
                moveByOne(headRow - 1, headColumn);
                break;
            case DOWN:
                moveByOne(headRow + 1, headColumn);
                break;
            case LEFT:
                moveByOne(headRow, headColumn - 1);
                break;
            case RIGHT:
                moveByOne(headRow, headColumn + 1);
                break;
        }
    }

    /**
     * Moves Snake by to the new position. When snake hits wall or the body isGameOver is set to true.
     *
     * @param row    where to move head
     * @param column where to move head
     */
    protected void moveByOne(int row, int column) {
        isGameOver = grid[row][column] == WALL || grid[row][column] == BODY;

        snake.get(0).isHead = false;
        snake.add(0, new BodyPart(true, row, column));

        if (grid[row][column] == FOOD) {
            placeFood();
            snakeScore += 1;
        } else {
            BodyPart toRemove = snake.get(snake.size() - 1);
            grid[toRemove.row][toRemove.column] = EMPTY;
            snake.remove(toRemove);
        }
    }

    /**
     * Maps current snake state to {@link SnakeDTO} which is used to pass state to {@link NeuralNetwork}
     *
     * @return DTO describing state
     */
    protected SnakeDTO snakeMapper() {
        SnakeDTO snakeDTO = new SnakeDTO();
        int wrongDirection = -1 * size;

        int rowDistance = snake.get(0).row - foodRow;
        if (rowDistance >= 0) {
            snakeDTO.upDistanceToFood = rowDistance;
            snakeDTO.downDistanceToFood = wrongDirection;
        } else {
            snakeDTO.downDistanceToFood = -1 * rowDistance;
            snakeDTO.upDistanceToFood = wrongDirection;
        }

        int columnDistance = snake.get(0).column - foodColumn;
        if (columnDistance >= 0) {
            snakeDTO.leftDistanceToFood = columnDistance;
            snakeDTO.rightDistanceToFood = wrongDirection;
        } else {
            snakeDTO.rightDistanceToFood = -1 * columnDistance;
            snakeDTO.leftDistanceToFood = wrongDirection;
        }

        int headRow = snake.get(0).row;
        int headColumn = snake.get(0).column;

        snakeDTO.leftSafe = (grid[headRow][headColumn - 1] == WALL || grid[headRow][headColumn - 1] == BODY) ? -1 : 1;
        snakeDTO.rightSafe = (grid[headRow][headColumn + 1] == WALL || grid[headRow][headColumn + 1] == BODY) ? -1 : 1;
        snakeDTO.upSafe = (grid[headRow - 1][headColumn] == WALL || grid[headRow - 1][headColumn] == BODY) ? -1 : 1;
        snakeDTO.downSafe = (grid[headRow + 1][headColumn] == WALL || grid[headRow + 1][headColumn] == BODY) ? -1 : 1;
        return snakeDTO;
    }

    /**
     * Plays game of {@link SnakeGame} with one {@link NeuralNetwork} for maxNumberOfMoves.
     *
     * @param neuralNetwork    that plays game
     * @param maxNumberOfMoves maximal number of snake moves so that snake won move in cycles
     */
    public void play(NeuralNetwork neuralNetwork, int maxNumberOfMoves) {
        double[] networkOutput;

        for (int i = 0; i < maxNumberOfMoves; i++) {
            networkOutput = neuralNetwork.getNetworkOutput(snakeMapper().getInput());
            moveSnake(outputToDirection(networkOutput));
            snakeToGrid();
            if (isGameOver)
                break;
        }
        neuralNetwork.score += snakeScore;
    }

    /**
     * should be replaced by gui version
     *
     * @param neuralNetwork    that plays game
     * @param maxNumberOfMoves maximal number of snake moves so that snake won move in cycles
     */
    public void showSnakeMoves(NeuralNetwork neuralNetwork, int maxNumberOfMoves) {
        double[] networkOutput;

        for (int i = 0; i < maxNumberOfMoves; i++) {
            networkOutput = neuralNetwork.getNetworkOutput(snakeMapper().getInput());
            moveSnake(outputToDirection(networkOutput));
            snakeToGrid();
            printSnakeGame();
            if (isGameOver)
                break;
        }
        neuralNetwork.score = snakeScore;
    }

    /**
     * The function converts output of {@link NeuralNetwork} to direction where to move.
     *
     * @param neuralNetworkOutput output of {@link NeuralNetwork}
     * @return direction where {@link NeuralNetwork decided to move
     */

    protected Direction outputToDirection(double[] neuralNetworkOutput) {
        Direction directionToMove = indexToDirection.get(maxValueIndex(neuralNetworkOutput));
        lastDirection = (lastDirection != Direction.opposite(directionToMove)) ? directionToMove : lastDirection;
        return lastDirection;
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
}
