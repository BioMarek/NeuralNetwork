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
    private int[][] grid;
    private final FreePosition freePosition;
    private final List<Snake> snakes = new ArrayList<>();

    public SnakeGameMultiplayer() {
        this.size = Settings.gridSize;
        initGrid();
        freePosition = new FreePosition(grid);
        reset();
    }

    @Override
    public int play(List<NeuralNetwork> neuralNetworks, int maxNumberOfMoves) {
        return 0;
    }

    @Override
    public void reset() {
        initGrid();
        placeSnakes();
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

    private void placeSnakes() {
        for (int i = 0; i < Settings.numOfPlayers; i++) {
            var position = freePosition.removeRandomFreeCoordinate();
            var snake = new Snake(position.getFirst(), position.getSecond(), Direction.randomDirection());
            snakes.add(snake);
        }
        // TODO put snakes onto grid
    }

    private void placeApple() {
        var position = freePosition.removeRandomFreeCoordinate();
        grid[position.getFirst()][position.getSecond()] = SnakeMap.FOOD.value;
    }
}
