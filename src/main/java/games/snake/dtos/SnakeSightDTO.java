package games.snake.dtos;

import games.freeEvolution.FESnake;
import games.snake.BodyPart;
import games.snake.Snake;
import games.snake.SnakeMap;
import utils.Settings;

import java.util.Arrays;


public class SnakeSightDTO {
    private final double[] result = new double[8];
    private final int[][] grid;
    private final int rows;
    private final int columns;

    public SnakeSightDTO(int[][] grid) {
        this.grid = grid;
        this.rows = grid.length;
        this.columns = grid[0].length;
    }

    public double[] getInput_8(Snake snake) {
        Arrays.fill(result, 0);
        calculateSightRay(snake, -1, 0, 0); // up
        calculateSightRay(snake, 0, 1, 1); // right
        calculateSightRay(snake, 1, 0, 2); // down
        calculateSightRay(snake, 0, -1, 3); // left

        calculateSightRay(snake, -1, 1, 4); // top right diagonal
        calculateSightRay(snake, 1, 1, 5); // bottom right diagonal
        calculateSightRay(snake, 1, -1, 6); // bottom left diagonal
        calculateSightRay(snake, -1, -1, 7); // top left diagonal
        return result;
    }

    public double[] getInput_8(FESnake snake) {
        // TODO make Snake and FEsnake into one interface
        Arrays.fill(result, 0);
        calculateSightRay(snake, -1, 0, 0); // up
        calculateSightRay(snake, 0, 1, 1); // right
        calculateSightRay(snake, 1, 0, 2); // down
        calculateSightRay(snake, 0, -1, 3); // left

        calculateSightRay(snake, -1, 1, 4); // top right diagonal
        calculateSightRay(snake, 1, 1, 5); // bottom right diagonal
        calculateSightRay(snake, 1, -1, 6); // bottom left diagonal
        calculateSightRay(snake, -1, -1, 7); // top left diagonal
        return result;
    }

    public double distanceCoefficient(int distance) {
        return 1.0d / distance;
    }

    public void calculateSightRay(Snake snake, int rowIncrease, int columnIncrease, int index) {
        BodyPart head = snake.bodyParts.get(0);
        for (int i = 1; i < Settings.SNAKE_SIGHT + 1; i++) {
            var currentRow = head.row + rowIncrease * i;
            var currentColumn = head.column + columnIncrease * i;
            if (Settings.HAS_WALL) {
                if (isOutOfBounds(currentRow, currentColumn))
                    break;
            } else {
                currentRow = wrapAroundCoordinates(currentRow, rows);
                currentColumn = wrapAroundCoordinates(currentColumn, columns);
            }
            if (grid[currentRow][currentColumn] == SnakeMap.FOOD.value) {
                result[index] = distanceCoefficient(i);
                break;
            }
            if (snake.isSnakeCollision(currentRow, currentColumn) || grid[currentRow][currentColumn] == SnakeMap.WALL.value) {
                result[index] = -1.0 * distanceCoefficient(i);
                break;
            }
        }
    }

    public void calculateSightRay(FESnake snake, int rowIncrease, int columnIncrease, int index) {
        BodyPart head = snake.bodyParts.get(0);
        for (int i = 1; i < Settings.SNAKE_SIGHT + 1; i++) {
            var currentRow = head.row + rowIncrease * i;
            var currentColumn = head.column + columnIncrease * i;
            if (Settings.HAS_WALL) {
                if (isOutOfBounds(currentRow, currentColumn))
                    break;
            } else {
                currentRow = wrapAroundCoordinates(currentRow, rows);
                currentColumn = wrapAroundCoordinates(currentColumn, columns);
            }
            if (grid[currentRow][currentColumn] == SnakeMap.FOOD.value) {
                result[index] = distanceCoefficient(i);
                break;
            }
            if (snake.isSnakeCollision(currentRow, currentColumn) || grid[currentRow][currentColumn] == SnakeMap.WALL.value) {
                result[index] = -1.0 * distanceCoefficient(i);
                break;
            }
        }
    }

    public int wrapAroundCoordinates(int coordinate, int max) {
        if (coordinate < 0) {
            return max + coordinate;
        }
        if (coordinate >= max)
            return coordinate - max;
        return coordinate;
    }

    public boolean isOutOfBounds(int row, int column) {
        return row < 0 || column < 0 || row >= rows || column >= columns;
    }
}
