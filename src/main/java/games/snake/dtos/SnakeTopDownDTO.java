package games.snake.dtos;

import games.snake.BodyPart;
import games.snake.Snake;
import utils.Settings;

import java.util.Arrays;

public class SnakeTopDownDTO {
    private final double[] result;
    public final int[][] grid;
    private final int rows;
    private final int columns;
    private final int sightSquareLength;

    public SnakeTopDownDTO(int[][] grid) {
        this.rows = grid.length + Settings.SNAKE_SIGHT * 2;
        this.columns = grid[0].length + Settings.SNAKE_SIGHT * 2;
        this.grid = new int[rows][columns];
        this.sightSquareLength = Settings.SNAKE_SIGHT * 2 + 1;
        this.result = new double[sightSquareLength * sightSquareLength];

        extendedCopyGrid(grid, Settings.SNAKE_SIGHT);
    }

    public void extendedCopyGrid(int[][] originalGrid, int offset) {
        for (int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], -1);
        }

        for (int row = offset; row < rows - offset; row++) {
            if (columns - offset * 2 >= 0)
                System.arraycopy(originalGrid[row - offset], 0, grid[row], offset, columns - offset - offset);
        }
    }

    public double[] getInput(Snake snake) {
        BodyPart head = snake.bodyParts.get(0);
        Arrays.fill(result, -1);
        var rowStart = Math.min(0, head.row - Settings.SNAKE_SIGHT / 2);
        var rowEnd = Math.max(rows, head.row + Settings.SNAKE_SIGHT / 2);
        var columnStart = Math.min(0, head.column - Settings.SNAKE_SIGHT / 2);
        var columnEnd = Math.max(columns, head.column + Settings.SNAKE_SIGHT / 2);

        return result;
    }
}
