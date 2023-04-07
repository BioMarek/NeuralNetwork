package games.snake;

import basic_neural_network.neural_network.BasicNeuralNetwork;
import games.Game;
import games.snake.dtos.SnakeBasicDTO;
import interfaces.NeuralNetwork;
import utils.Direction;
import utils.Settings;
import utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Games.Snake game it used check whether {@link BasicNeuralNetwork} can play simple game.
 */
public class SnakeGame implements Game {

    private final int size;
    public int[][] grid;

    protected List<BodyPart> bodyParts;
    protected int foodRow;
    protected int foodColumn;

    public Direction lastDirection;
    public int snakeScore;
    public boolean isGameOver = false;
    private SnakeBasicDTO snakeBasicDTO = new SnakeBasicDTO();

    public SnakeGame() {
        this.size = Settings.gridSize;;
        reset();
    }

    /**
     * Resets game into initial state.
     */
    @Override
    public void reset() {
        initSnake();
        placeFood();
        snakeToGrid();
    }

    /**
     * Creates initial snake game setup
     */
    protected void initSnake() {
        snakeScore = 0;
        lastDirection = Direction.UP;
        this.grid = new int[size][size];

        bodyParts = new ArrayList<>();
        bodyParts.add(new BodyPart(true, size / 2, size / 2));
        bodyParts.add(new BodyPart(false, size / 2 + 1, size / 2));
        bodyParts.add(new BodyPart(false, size / 2 + 2, size / 2));

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (row == 0 || row == size - 1 || column == 0 || column == size - 1)
                    grid[row][column] = SnakeMap.WALL.value;
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
            if (grid[row][column] != SnakeMap.EMPTY.value) {
                row = Util.randomInt(1, size - 1);
                column = Util.randomInt(1, size - 1);
            } else {
                foodRow = row;
                foodColumn = column;
                grid[row][column] = SnakeMap.FOOD.value;
                return;
            }
        }
    }

    /**
     * Places snake into grid.
     */
    protected void snakeToGrid() {
        bodyParts.forEach((bodyPart) -> grid[bodyPart.row][bodyPart.column] = bodyPart.isHead ? SnakeMap.HEAD.value : SnakeMap.BODY.value);
    }

    /**
     * Prints snakeGame using ascii characters.
     */
    public void printSnakeGame() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                switch (grid[row][column]) {
                    case 0 -> System.out.print(" ");
                    case 1 -> System.out.print("X");
                    case 2 -> System.out.print("O");
                    case 3 -> System.out.print("B");
                    case 4 -> System.out.print("H");
                }
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
        Direction directionToMove = null;
        switch (key) {
            case "w" -> directionToMove = Direction.UP;
            case "s" -> directionToMove = Direction.DOWN;
            case "a" -> directionToMove = Direction.LEFT;
            case "d" -> directionToMove = Direction.RIGHT;
        }
        lastDirection = directionToMove == null || lastDirection == Direction.opposite(directionToMove) ? lastDirection : directionToMove;
        return lastDirection;
    }

    /**
     * Moves Games.Snake to given direction
     *
     * @param direction where to move snake
     */
    protected void moveSnake(Direction direction) {
        int headRow = bodyParts.get(0).row;
        int headColumn = bodyParts.get(0).column;
        switch (direction) {
            case UP -> moveByOne(headRow - 1, headColumn);
            case DOWN -> moveByOne(headRow + 1, headColumn);
            case LEFT -> moveByOne(headRow, headColumn - 1);
            case RIGHT -> moveByOne(headRow, headColumn + 1);
        }
    }

    /**
     * Moves Games.Snake by to the new position. When snake hits wall or the body isGameOver is set to true.
     *
     * @param row    where to move head
     * @param column where to move head
     */
    protected void moveByOne(int row, int column) {
        isGameOver = grid[row][column] == SnakeMap.WALL.value || grid[row][column] == SnakeMap.BODY.value;

        bodyParts.get(0).isHead = false;
        bodyParts.add(0, new BodyPart(true, row, column));

        if (grid[row][column] == SnakeMap.FOOD.value) {
            placeFood();
            snakeScore += 1;
        } else {
            BodyPart toRemove = bodyParts.get(bodyParts.size() - 1);
            grid[toRemove.row][toRemove.column] = SnakeMap.EMPTY.value;
            bodyParts.remove(toRemove);
        }
    }

    /**
     * Plays game of {@link SnakeGame} with one {@link NeuralNetwork} for maxNumberOfMoves.
     *
     * @param neuralNetwork    that plays game
     * @param maxNumberOfMoves maximal number of snake moves so that snake won move in cycles
     * @return score {@link NeuralNetwork} achieved in {@link Game}
     */
    @Override
    public int play(NeuralNetwork neuralNetwork, int maxNumberOfMoves) {
        double[] networkOutput;

        for (int i = 0; i < maxNumberOfMoves; i++) {
            networkOutput = neuralNetwork.getNetworkOutput(snakeBasicDTO.snakeMapper(bodyParts, grid, foodRow, foodColumn).getNeuralNetworkInput());
            moveSnake(outputToDirection(networkOutput));
            snakeToGrid();
            if (isGameOver)
                break;
        }
        return snakeScore;
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
            networkOutput = neuralNetwork.getNetworkOutput(snakeBasicDTO.snakeMapper(bodyParts, grid, foodRow, foodColumn).getNeuralNetworkInput());
            moveSnake(outputToDirection(networkOutput));
            snakeToGrid();
            printSnakeGame();
            if (isGameOver)
                break;
        }
    }

    /**
     * The function converts output of {@link NeuralNetwork} to direction where to move.
     *
     * @param neuralNetworkOutput output of {@link NeuralNetwork}
     * @return direction where {@link NeuralNetwork decided to move
     */
    protected Direction outputToDirection(double[] neuralNetworkOutput) {
        Direction directionToMove = lastDirection;
        switch (maxValueIndex(neuralNetworkOutput)) {
            case 0 -> directionToMove = Direction.UP;
            case 1 -> directionToMove = Direction.DOWN;
            case 2 -> directionToMove = Direction.LEFT;
            case 3 -> directionToMove = Direction.RIGHT;

        }
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
