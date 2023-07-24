package games.snake.dtos;

import games.snake.freeEvolution.FESnake;
import games.snake.BodyPart;
import games.snake.Snake;
import utils.Settings;

import java.util.Arrays;

public class SnakeTopDownDTO implements SnakeSightDTO{
    private final double[] result;
    public final int[][] grid;
    private final int rows;
    private final int columns;
    private final int snakeSight;

    public SnakeTopDownDTO(int[][] grid) {
        this.snakeSight = Settings.SNAKE_SIGHT;
        this.rows = grid.length + snakeSight * 2;
        this.columns = grid[0].length + snakeSight * 2;
        this.grid = new int[rows][columns];
        int sightSquareLength = snakeSight * 2 + 1;
        this.result = new double[sightSquareLength * sightSquareLength - 1];

        extendedCopyGrid(grid, snakeSight);
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
        var rowEnd = head.row + snakeSight * 2 + 1;
        var columnEnd = head.column + snakeSight * 2 + 1;
        int resultIndex = 0;

        for (int rowStart = head.row; rowStart < rowEnd; rowStart++) {
            for (int columnStart = head.column; columnStart < columnEnd; columnStart++) {
                if (rowStart == (head.row + snakeSight) && columnStart == (head.column + snakeSight))
                    continue;
                result[resultIndex] = grid[rowStart][columnStart];
                resultIndex++;
            }
        }
        return result;
    }

    @Override
    public double[] getInput(FESnake snake) {
        BodyPart head = snake.bodyParts.get(0);
        var rowEnd = head.row + snakeSight * 2 + 1;
        var columnEnd = head.column + snakeSight * 2 + 1;
        int resultIndex = 0;

        for (int rowStart = head.row; rowStart < rowEnd; rowStart++) {
            for (int columnStart = head.column; columnStart < columnEnd; columnStart++) {
                if (rowStart == (head.row + snakeSight) && columnStart == (head.column + snakeSight))
                    continue;
                result[resultIndex] = grid[rowStart][columnStart];
                resultIndex++;
            }
        }
        return result;
    }
}
