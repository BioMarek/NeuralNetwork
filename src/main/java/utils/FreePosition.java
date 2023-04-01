package utils;

import games.snake.SnakeMap;

import java.util.ArrayList;
import java.util.List;


/**
 * For given grid generates empty positions and can return random empty position. Grid positions containing walls
 * are not returned. Returned random position is expected to be occupied, so it is removed from list of free positions.
 */
public class FreePosition {
    public final List<Pair<Integer>> availableCoordinates;

    /**
     * To slow to use every time snake dies or food needs to be replaced. Only useful for big and almost full grid.
     */
    public FreePosition(int[][] grid) {
        availableCoordinates = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid.length; column++) {
                if (grid[row][column] != SnakeMap.WALL.value) {
                    var pair = new Pair<>(row, column);
                    availableCoordinates.add(pair);
                }
            }
        }
    }

    public Pair<Integer> removeRandomFreeCoordinate() {
        if (availableCoordinates.size() == 0)
            throw new RuntimeException("in removeRandomFreeCoordinate all were coordinates used up");

        var i = Util.randomInt(0, availableCoordinates.size());
        var coordinate = availableCoordinates.get(i);
        availableCoordinates.remove(coordinate);
        return coordinate;
    }

    public static Pair<Integer> randomFreeCoordinate(int[][] grid) {
        while (true) {
            int row = Util.randomInt(1, grid.length - 1);
            int column = Util.randomInt(1, grid.length - 1);
            if (grid[row][column] != SnakeMap.EMPTY.value) {
                row = Util.randomInt(1, grid.length - 1);
                column = Util.randomInt(1, grid.length - 1);
            } else {
                return new Pair<>(row, column);
            }
        }
    }
}
