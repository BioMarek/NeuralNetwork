package games.snake;

import games.snake.dtos.SnakeTopDownDTO;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Settings;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SnakeTopDownDTOTest {
    @Test
    public void testExtendedCopyGrid_SightIs1() {
        int originalSight = Settings.SNAKE_SIGHT;
        Settings.SNAKE_SIGHT = 1;

        int[][] originalGrid = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int[][] expectedGrid = {
                {-1, -1, -1, -1, -1},
                {-1, 1, 2, 3, -1},
                {-1, 4, 5, 6, -1},
                {-1, 7, 8, 9, -1},
                {-1, -1, -1, -1, -1}
        };

        SnakeTopDownDTO snakeTopDownDTO = new SnakeTopDownDTO(originalGrid);
        assertThat(Arrays.deepEquals(snakeTopDownDTO.grid, expectedGrid), is(true));

        Settings.SNAKE_SIGHT = originalSight;
    }

    @Test
    public void testExtendedCopyGrid_SightIs2() {
        int originalSight = Settings.SNAKE_SIGHT;
        Settings.SNAKE_SIGHT = 2;

        int[][] originalGrid = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int[][] expectedGrid = {
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, 1, 2, 3, -1, -1},
                {-1, -1, 4, 5, 6, -1, -1},
                {-1, -1, 7, 8, 9, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1},
        };

        SnakeTopDownDTO snakeTopDownDTO = new SnakeTopDownDTO(originalGrid);
        assertThat(Arrays.deepEquals(snakeTopDownDTO.grid, expectedGrid), is(true));

        Settings.SNAKE_SIGHT = originalSight;
    }

    @Test
    public void testExtendedCopyGrid_GetInput() {
        int originalSight = Settings.SNAKE_SIGHT;
        Settings.SNAKE_SIGHT = 1;

        int[][] originalGrid = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 1, 1}
        };
        Snake snake = new Snake(originalGrid, 0, 0, Direction.UP, 1);
        double[] result = {-1, -1, -1, -1, 0, -1, 0, 1};
        SnakeTopDownDTO snakeTopDownDTO = new SnakeTopDownDTO(originalGrid);
        var output = snakeTopDownDTO.getInput(snake);

        assertThat(Arrays.equals(output, result), is(true));

        Settings.SNAKE_SIGHT = originalSight;
    }
}
