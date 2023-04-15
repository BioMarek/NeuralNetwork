package games.snake.dtos;

import games.snake.BodyPart;
import games.snake.Snake;
import games.snake.SnakeMap;

import static utils.Settings.SNAKE_SIGHT;

public class SnakeSightDTO {
    private final double[] result = new double[8];
    private final int[][] grid;

    public SnakeSightDTO(int[][] grid) {
        this.grid = grid;
    }

    public double[] getInput_8(Snake snake) {
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

    public float distanceCoefficient(int distance) {
        return 1.0f / distance;
    }

    public void calculateSightRay(Snake snake, int rowIncrease, int columnIncrease, int index) {
        BodyPart head = snake.bodyParts.get(0);
        for (int i = 1; i < SNAKE_SIGHT + 1; i++) {
            var currentRow = head.row + rowIncrease * i;
            var currentColumn = head.column + columnIncrease * i;
            if (isOutOfBounds(grid, currentRow, currentColumn))
                break;
            if (grid[currentRow][currentColumn] == SnakeMap.FOOD.value) {
                result[index] = 1.0 * distanceCoefficient(i);
                break;
            }
            if (snake.isAnotherSnake(grid, currentRow, currentColumn) || grid[currentRow][currentColumn] == SnakeMap.WALL.value) {
                result[index] = -1.0 * distanceCoefficient(i);
                break;
            }
        }
    }

    public boolean isOutOfBounds(int[][] grid, int row, int column) {
        return row < 0 || column < 0 || row >= grid.length || column >= grid.length;
    }
}
