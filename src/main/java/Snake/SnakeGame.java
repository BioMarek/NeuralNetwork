package Snake;

import NeuralNetwork.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SnakeGame {
    private final int WALL = 1;
    private final int BODY = 2;
    private final int HEAD = 3;
    private final int FOOD = 4;

    private final int[][] grid;
    private final int size;
    private final List<BodyPart> snake;
    private Direction lastDirection;

    public SnakeGame(int size) {
        this.size = size;
        this.grid = new int[size][size];
        this.lastDirection = Direction.UP;

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
        putFood();
        snakeToGrid();
        printSnake();
    }

    public void mainLoop() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String move = scanner.next();
            boolean gameOver = false;
            switch (move) {
                case "w":
                    gameOver = moveSnake(Direction.UP);
                    break;
                case "s":
                    gameOver = moveSnake(Direction.DOWN);
                    break;
                case "a":
                    gameOver = moveSnake(Direction.LEFT);
                    break;
                case "d":
                    gameOver = moveSnake(Direction.RIGHT);
                    break;
            }
            snakeToGrid();
            printSnake();
            System.out.println(gameOver);

            if (gameOver)
                return;
        }
    }

    public void putFood() {
        while (true) {
            int x = Util.randomInt(1, size - 1);
            int y = Util.randomInt(1, size - 1);
            if (grid[x][y] == HEAD || grid[x][y] == BODY) {
                x = Util.randomInt(1, size - 1);
                y = Util.randomInt(1, size - 1);
            } else {
                grid[x][y] = FOOD;
                break;
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

    public boolean moveByOne(int row, int column){
        if (grid[row][column] == WALL || grid[row][column] == BODY)
            return true;
        if (grid[row][column] == FOOD) {
            snake.get(0).head = false;
            snake.add(0, new BodyPart(true, row, column));
            putFood();
        } else {
            snake.get(0).head = false;
            snake.add(0, new BodyPart(true, row, column));
            BodyPart toRemove = snake.get(snake.size() - 1);
            grid[toRemove.row][toRemove.column] = 0;
            snake.remove(toRemove);
        }
        lastDirection = Direction.LEFT;
        return false;
    }

    public void snakeToGrid() {
        for (BodyPart bodyPart : snake) {
            if (bodyPart.head)
                grid[bodyPart.row][bodyPart.column] = HEAD;
            else
                grid[bodyPart.row][bodyPart.column] = BODY;
        }
    }

    public void printSnake() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                switch (grid[row][column]) {
                    case WALL:
                        System.out.print((char) 182);
                        break;
                    case HEAD:
                        System.out.print("H");
                        break;
                    case BODY:
                        System.out.print("B");
                        break;
                    case FOOD:
                        System.out.print("O");
                        break;
                    default:
                        System.out.print(" ");
                }
            }
            System.out.println("");
        }
    }
}
