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

    private final int[][] grid;
    private final int size;
    protected List<BodyPart> snake;
    protected int foodRow;
    protected int foodColumn;

    public Direction lastDirection = Direction.UP;
    public int snakeScore = 0;
    public boolean isGameOver = false;

    public SnakeGame(int size) {
        this.size = size;
        this.grid = new int[size][size];

        initSnake();
        placeFood();
        snakeToGrid();
    }

    /**
     * Creates initial snake game setup
     */
    public void initSnake() {
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
    public void placeFood() {
        while (true) {
            int row = Util.randomInt(1, size - 1);
            int column = Util.randomInt(1, size - 1);
            if (grid[row][column] == HEAD || grid[row][column] == BODY) {
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
    public void snakeToGrid() {
        snake.forEach((bodyPart) -> grid[bodyPart.row][bodyPart.column] = bodyPart.isHead ? HEAD : BODY);
    }

    /**
     * Prints snakeGame using asci characters.
     */
    public void printSnakeGame() {
        Map<Integer, String> tiles = new HashMap<>();
        tiles.put(WALL, "X");
        tiles.put(HEAD, "H");
        tiles.put(BODY, "B");
        tiles.put(FOOD, "O");
        tiles.put(EMPTY, " ");

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                System.out.print(tiles.get(grid[row][column]));
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
            moveSnake(keyToDirection(move));
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
    public Direction keyToDirection(String key) {
        Map<String, Direction> keyToDirection = new HashMap<>();
        keyToDirection.put("w", Direction.UP);
        keyToDirection.put("s", Direction.DOWN);
        keyToDirection.put("a", Direction.LEFT);
        keyToDirection.put("d", Direction.RIGHT);

        if (keyToDirection.get(key) == null || lastDirection == Direction.opposite(keyToDirection.get(key))) {
            return lastDirection;
        }
        lastDirection = keyToDirection.get(key);

        return lastDirection;
    }

    /**
     * Moves Snake to given direction
     *
     * @param direction where to move snake
     */
    public void moveSnake(Direction direction) {
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
    public void moveByOne(int row, int column) {
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
    public SnakeDTO snakeMapper() {
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
     * Calculates one snake move after {@link NeuralNetwork} input.
     *
     * @param move supplied by {@link NeuralNetwork}
     */
    public void processNeuralNetworkMove(String move) {
        moveSnake(keyToDirection(move));
        snakeToGrid();
    }

    /**
     * Plays one game of {@link SnakeGame} with one {@link NeuralNetwork}
     *
     * @param neuralNetwork that plays game
     */
    public void play(NeuralNetwork neuralNetwork, int maxNumberOfMoves) {
        SnakeGame snakeGame = new SnakeGame(20);
        double[] networkOutput;

        for (int i = 0; i < maxNumberOfMoves; i++) {
            networkOutput = neuralNetwork.getNetworkOutput(snakeGame.snakeMapper().getInput());
            snakeGame.processNeuralNetworkMove(translateOutputToKey(networkOutput));
            if (snakeGame.isGameOver)
                break;
        }
        neuralNetwork.score = snakeGame.snakeScore;
    }

    public String translateOutputToKey(double[] neuralNetworkOutput) {
        int maxIndex = 0;
        double max = neuralNetworkOutput[0];

        for (int i = 0; i < neuralNetworkOutput.length; i++) {
            if (max < neuralNetworkOutput[i]) {
                max = neuralNetworkOutput[i];
                maxIndex = i;
            }
        }

        switch (maxIndex) {
            case 0:
                return "w";
            case 1:
                return "s";
            case 2:
                return "a";
            case 3:
                return "d";
        }

        return "error";
    }
}
