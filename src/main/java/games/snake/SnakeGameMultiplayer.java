package games.snake;

import games.MultiplayerGame;
import interfaces.NeuralNetwork;
import utils.Direction;
import utils.FreePosition;
import utils.Settings;

import java.util.ArrayList;
import java.util.List;

import static utils.Util.repeat;

public class SnakeGameMultiplayer implements MultiplayerGame {
    private final int size;
    protected int[][] grid;
    protected final List<Snake> snakes = new ArrayList<>();

    public SnakeGameMultiplayer() {
        this.size = Settings.gridSize;
        reset();
    }

    @Override
    public int play(List<NeuralNetwork> neuralNetworks, int maxNumberOfMoves) {
        return 0;
    }

    @Override
    public void reset() {
        initGrid();
        initSnakes();
        repeat.accept(Settings.numOfApples, this::placeFood);
    }

    private void initGrid() {
        this.grid = new int[size][size];

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (Settings.hasWalls && (row == 0 || row == size - 1 || column == 0 || column == size - 1))
                    grid[row][column] = SnakeMap.WALL.value;
            }
        }
    }

    private void initSnakes() {
        for (int i = 0; i < Settings.numOfPlayers; i++) {
            var coordinates = FreePosition.randomFreeCoordinate(grid);
            var snake = new Snake(coordinates.getFirst(), coordinates.getSecond(), Direction.randomDirection(), i);
            placeSnake(snake);
            snakes.add(snake);
        }
    }

    protected void placeSnake(Snake snake) {
        var bodyParts = snake.bodyParts;
        for (int j = bodyParts.size() - 1; j >= 0; j--) { // head will be always on top of other bodyparts
            var bodyPart = bodyParts.get(j);
            if (bodyPart.isHead)
                grid[bodyPart.row][bodyPart.column] = snake.name + 200;
            else
                grid[bodyPart.row][bodyPart.column] = snake.name + 100;
        }
    }

    public void removeSnake(Snake snake) {
        for (BodyPart bodyPart : snake.bodyParts) {
            grid[bodyPart.row][bodyPart.column] = SnakeMap.EMPTY.value;
        }
    }

    private void placeFood() {
        var coordinates = FreePosition.randomFreeCoordinate(grid);
        grid[coordinates.getFirst()][coordinates.getSecond()] = SnakeMap.FOOD.value;
    }

    /**
     * Moves Games.Snake to given direction
     *
     * @param direction where to move snake
     */
    protected void moveSnakeToDirection(Snake snake, Direction direction) {
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
        if (snakeCollision(row, column)) {
            var coordinates = FreePosition.randomFreeCoordinate(grid);
            removeSnake(snake);
            snake.resetSnake(coordinates.getFirst(), coordinates.getSecond(), Direction.randomDirection());
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
            placeFood();
            snake.snakeScore += 1;
        } else {
            removeSnake(snake);
            bodyParts.remove(bodyParts.size() - 1);
            placeSnake(snake);
        }
    }

    /**
     * Checks whether snake can move specific grid coordinates.
     *
     * @return true moving to coordinates will result in death
     */
    protected boolean snakeCollision(int row, int column) {
        return grid[row][column] == SnakeMap.WALL.value || grid[row][column] >= 100;
    }


    /**
     * Prints snakeGame using ascii characters.
     */
    public void printSnakeGame() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (grid[row][column] >= 100)
                    System.out.print(grid[row][column]);
                else
                    System.out.print(" " + grid[row][column] + " ");
            }
            System.out.println();
        }
    }
}
