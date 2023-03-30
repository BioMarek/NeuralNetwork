package games.snake;

import games.MultiplayerGame;
import interfaces.NeuralNetwork;
import utils.Settings;

import java.util.List;

public class SnakeGameMultiplayer implements MultiplayerGame {
    private final int size;
    private int[][] grid;

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
}
