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
    protected List<BodyPart> snake;
    protected int foodRow;
    protected int foodColumn;
    public Direction lastDirection;

    public SnakeGame(int size) {
        this.size = size;
        this.grid = new int[size][size];
        this.lastDirection = Direction.UP;

        initSnake();
        placeFood();
        snakeToGrid();
        printSnake();
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

    public void snakeToGrid() {
        for (BodyPart bodyPart : snake) {
            grid[bodyPart.row][bodyPart.column] = bodyPart.isHead ? HEAD : BODY;
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

    public boolean moveSnake(Direction direction) {
        boolean gameOver = false;
        switch (direction) {
            case UP:
                gameOver = moveByOne(snake.get(0).row - 1, snake.get(0).column);
                break;
            case DOWN:
                gameOver = moveByOne(snake.get(0).row + 1, snake.get(0).column);
                break;
            case LEFT:
                gameOver = moveByOne(snake.get(0).row, snake.get(0).column - 1);
                break;
            case RIGHT:
                gameOver = moveByOne(snake.get(0).row, snake.get(0).column + 1);
                break;
        }
        return gameOver;
    }

    public boolean moveByOne(int row, int column) {
        if (grid[row][column] == WALL || grid[row][column] == BODY)
            return true;

        snake.get(0).isHead = false;
        snake.add(0, new BodyPart(true, row, column));

        if (grid[row][column] == FOOD) {
            placeFood();
        } else {
            BodyPart toRemove = snake.get(snake.size() - 1);
            grid[toRemove.row][toRemove.column] = EMPTY;
            snake.remove(toRemove);
        }
        return false;
    }

    public SnakeDTO snakeMapper() {
        SnakeDTO snakeDTO = new SnakeDTO();

        int rowDistance = snake.get(0).row - foodRow;
        if (rowDistance >= 0) {
            snakeDTO.upDistanceToFood = rowDistance;
            snakeDTO.downDistanceToFood = -1 * size;
        } else {
            snakeDTO.downDistanceToFood = -1 * rowDistance;
            snakeDTO.upDistanceToFood = -1 * size;
        }

        int columnDistance = snake.get(0).column - foodColumn;
        if (columnDistance >= 0) {
            snakeDTO.leftDistanceToFood = columnDistance;
            snakeDTO.rightDistanceToFood = -1 * size;
        } else {
            snakeDTO.rightDistanceToFood = -1 * columnDistance;
            snakeDTO.leftDistanceToFood = -1 * size;
        }

        int snakeRow = snake.get(0).row;
        int snakeColumn = snake.get(0).column;

        snakeDTO.leftSafe = (grid[snakeRow][snakeColumn - 1] == WALL || grid[snakeRow][snakeColumn - 1] == BODY) ? -1 : 1;
        snakeDTO.rightSafe = (grid[snakeRow][snakeColumn + 1] == WALL || grid[snakeRow][snakeColumn + 1] == BODY) ? -1 : 1;
        snakeDTO.upSafe = (grid[snakeRow - 1][snakeColumn] == WALL || grid[snakeRow - 1][snakeColumn] == BODY) ? -1 : 1;
        snakeDTO.downSafe = (grid[snakeRow + 1][snakeColumn] == WALL || grid[snakeRow + 1][snakeColumn] == BODY) ? -1 : 1;
        return snakeDTO;
    }
}
