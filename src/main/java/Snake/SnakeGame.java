package Snake;

import NeuralNetwork.Util;

import java.util.*;

/**
 * Snake game it used check whether {@link NeuralNetwork} can play simple game.
 */
public class SnakeGame {
    private final int EMPTY = 0;
    private final int WALL = 1;
    private final int BODY = 2;
    private final int HEAD = 3;
    private final int FOOD = 4;

    private final int[][] grid;
    private final int size;
    private List<BodyPart> snake;
    private Direction lastDirection;

    public SnakeGame(int size) {
        this.size = size;
        this.grid = new int[size][size];
        this.lastDirection = Direction.UP;

        initSnake();
        placeFood();
        snakeToGrid();
        printSnake();
    }

    public void mainLoop() {
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;
        while (!gameOver) {
            String move = scanner.next();
            gameOver = moveSnake(keyToDirection(move));
            snakeToGrid();
            printSnake();
        }
    }

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

    public void placeFood() {
        while (true) {
            int x = Util.randomInt(1, size - 1);
            int y = Util.randomInt(1, size - 1);
            if (grid[x][y] == HEAD || grid[x][y] == BODY) {
                x = Util.randomInt(1, size - 1);
                y = Util.randomInt(1, size - 1);
            } else {
                grid[x][y] = FOOD;
                return;
            }
        }
    }

    public boolean moveSnake(Direction direction) {
        boolean gameOver = false;
        switch (direction) {
            case UP:
                gameOver = moveByOne(snake.get(0).row - 1, snake.get(0).column);
                lastDirection = Direction.UP;
                break;
            case DOWN:
                gameOver = moveByOne(snake.get(0).row + 1, snake.get(0).column);
                lastDirection = Direction.DOWN;
                break;
            case LEFT:
                gameOver = moveByOne(snake.get(0).row, snake.get(0).column - 1);
                lastDirection = Direction.LEFT;
                break;
            case RIGHT:
                gameOver = moveByOne(snake.get(0).row, snake.get(0).column + 1);
                lastDirection = Direction.LEFT;
                break;
        }
        return gameOver;
    }

    public Direction keyToDirection(String key) {
        switch (key) {
            case "w":
                lastDirection = Direction.UP;
                return Direction.UP;
            case "s":
                lastDirection = Direction.DOWN;
                return Direction.DOWN;
            case "a":
                lastDirection = Direction.LEFT;
                return Direction.LEFT;
            case "d":
                lastDirection = Direction.RIGHT;
                return Direction.RIGHT;
        }
        return lastDirection;
    }

    public boolean moveByOne(int row, int column) {
        if (grid[row][column] == WALL || grid[row][column] == BODY)
            return true;
        if (grid[row][column] == FOOD) {
            snake.get(0).head = false;
            snake.add(0, new BodyPart(true, row, column));
            placeFood();
        } else {
            snake.get(0).head = false;
            snake.add(0, new BodyPart(true, row, column));
            BodyPart toRemove = snake.get(snake.size() - 1);
            grid[toRemove.row][toRemove.column] = EMPTY;
            snake.remove(toRemove);
        }
        return false;
    }

    public void snakeToGrid() {
        for (BodyPart bodyPart : snake) {
            grid[bodyPart.row][bodyPart.column] = bodyPart.head ? HEAD : BODY;
        }
    }

    public void printSnake() {
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
}
