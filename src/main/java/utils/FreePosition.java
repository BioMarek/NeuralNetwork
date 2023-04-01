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
}
