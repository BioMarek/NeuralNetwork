package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.FreePosition;
import utils.Pair;

public class FreePositionTest {
    private FreePosition freePosition;

    @BeforeEach
    public void setUp() {
        int[][] grid = {{0, 0}, {0, 0}};
        freePosition = new FreePosition(grid);
    }

    @Test
    public void testRemoveRandomFreeCoordinate() {
        Pair<Integer> coordinate = freePosition.removeRandomFreeCoordinate();
        Assertions.assertNotNull(coordinate);
        Assertions.assertTrue(freePosition.availableCoordinates.size() < 4);
    }

    @Test
    public void testAllCoordinatesRemoved() {
        int count = 0;
        int initialSize = freePosition.availableCoordinates.size();
        for (int i = 0; i < initialSize; i++) {
            Pair<Integer> coordinate = freePosition.removeRandomFreeCoordinate();
            count++;
            Assertions.assertNotNull(coordinate);
        }
        Assertions.assertEquals(0, freePosition.availableCoordinates.size());
        Assertions.assertEquals(count, 4);
    }
}
