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
        repeat.accept(Settings.numOfApples, this::placeApple);
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
            var snake = new Snake(coordinates.getFirst(), coordinates.getSecond(), Direction.randomDirection());
            placeSnake(snake, i);
            snakes.add(snake);
        }
    }

    private void placeSnake(Snake snake, int i) {
        var bodyParts = snake.bodyParts;
        for (int j = bodyParts.size() - 1; j >= 0; j--) { // head will be always on top of other bodyparts
            var bodyPart = bodyParts.get(j);
            if (bodyPart.isHead)
                grid[bodyPart.row][bodyPart.column] = i + 200;
            else
                grid[bodyPart.row][bodyPart.column] = i + 100;
        }

    }

    private void placeApple() {
        var coordinates = FreePosition.randomFreeCoordinate(grid);
        grid[coordinates.getFirst()][coordinates.getSecond()] = SnakeMap.FOOD.value;
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
