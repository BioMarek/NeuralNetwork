package games.snake;

import games.snake.dtos.SnakeSightDTO;
import games.snake.savegame.SaveGameUtil;
import games.snake.savegame.SavedGameDTO;
import neat.phenotype.NeuralNetwork;
import utils.Direction;
import utils.Settings;
import utils.Util;

import static utils.Util.randomCoordinate;
import static utils.Util.randomFreeCoordinate;

public abstract class AbstractSnakeGame {
    protected SnakeSightDTO snakeSightDTO;
    protected int columns;
    protected int rows;

    public int[][] grid;
    public int numOfFood;

    protected void initGrid() {
        this.grid = new int[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (Settings.HAS_WALL && (row == 0 || row == rows - 1 || column == 0 || column == columns - 1))
                    grid[row][column] = SnakeMap.WALL.value;
            }
        }
    }

    /**
     * If there is less food on the grid then Settings.maxNumberOfFood one additional food will be added on random grid
     * square.
     */
    protected void placeFood() {
        if (numOfFood >= Settings.MAX_NUM_OF_FOOD) {
            return;
        }

        var coordinates = Settings.IS_FOOD_GUARANTEED ? randomFreeCoordinate(grid) : randomCoordinate(grid);
        if (grid[coordinates.getFirst()][coordinates.getSecond()] == SnakeMap.EMPTY.value) {
            grid[coordinates.getFirst()][coordinates.getSecond()] = SnakeMap.FOOD.value;
            numOfFood++;
        }
    }

    protected int wrapAroundCoordinates(int coordinate, int max) {
        if (coordinate == -1)
            return max - 1;
        if (coordinate == max)
            return 0;
        return coordinate;
    }

    /**
     * The function converts output of {@link NeuralNetwork} to direction where to move.
     *
     * @param neuralNetworkOutput output of {@link NeuralNetwork}
     * @return direction where {@link NeuralNetwork decided to move
     */
    protected Direction outputToDirection(double[] neuralNetworkOutput) {
        return switch (maxValueIndex(neuralNetworkOutput)) {
            case 0 -> Direction.UP;
            case 1 -> Direction.RIGHT;
            case 2 -> Direction.DOWN;
            case 3 -> Direction.LEFT;
            default -> Direction.NONE;
        };
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

    public void setSaveGameMetadata(SavedGameDTO savedGameDTO) {
        savedGameDTO.rows = rows;
        savedGameDTO.columns = columns;
        savedGameDTO.fileName = SaveGameUtil.getCurrentDateTimeAsString() + ".sav";
    }

    /**
     * Prints snakeGame using ascii characters.
     */
    public void printSnakeGame() {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (grid[row][column] >= SnakeMap.BODY.value)
                    System.out.print(grid[row][column]);
                else
                    System.out.print(" " + grid[row][column] + " ");
            }
            System.out.println();
        }
    }
}
