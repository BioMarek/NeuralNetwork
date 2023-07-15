package games.snake;

import games.snake.dtos.SnakeSightRaysDTO;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Settings;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

public class SnakeSightRaysDTOTest {
    private static int SIZE = 4;
    private int[][] grid;
    private SnakeSightRaysDTO snakeSightRaysDTO;


    @Test
    void calculateSightRay_isCorrectGridFour() {
        Settings.SNAKE_SIGHT = 4;
        SIZE = 4;
        initGrid();
        var input = snakeSightRaysDTO.getInput(new Snake(grid, 1, 1, Direction.UP, 1));
        assertThat(input[0], is(closeTo(-1.0, 0.1)));
        assertThat(input[1], is(closeTo(-0.5, 0.1)));
        assertThat(input[2], is(closeTo(-0.5, 0.1)));
        assertThat(input[3], is(closeTo(-1.0, 0.1)));

        assertThat(input[4], is(closeTo(-1.0, 0.1)));
        assertThat(input[5], is(closeTo(-0.5, 0.1)));
        assertThat(input[6], is(closeTo(-1.0, 0.1)));
        assertThat(input[7], is(closeTo(-1.0, 0.1)));
    }

    @Test
    void calculateSightRay_isCorrectGridFive() {
        Settings.SNAKE_SIGHT = 4;
        SIZE = 5;
        initGrid();
        grid[3][3] = SnakeMap.FOOD.value;
        grid[2][3] = SnakeMap.FOOD.value;
        grid[1][3] = 301;
        var input = snakeSightRaysDTO.getInput(new Snake(grid, 2, 2, Direction.UP, 1));
        assertThat(input[0], is(closeTo(-0.5, 0.1)));
        assertThat(input[1], is(closeTo(1.0, 0.1)));
        assertThat(input[2], is(closeTo(-0.5, 0.1)));
        assertThat(input[3], is(closeTo(-0.5, 0.1)));

        assertThat(input[4], is(closeTo(-1.0, 0.1)));
        assertThat(input[5], is(closeTo(1.0, 0.1)));
        assertThat(input[6], is(closeTo(-0.5, 0.1)));
        assertThat(input[7], is(closeTo(-0.5, 0.1)));
    }

    @Test
    void calculateSightRay_isCorrectGridTen() {
        Settings.SNAKE_SIGHT = 4;
        SIZE = 10;
        initGrid();
        grid[4][8] = SnakeMap.FOOD.value;
        grid[2][2] = SnakeMap.FOOD.value;
        grid[4][5] = 101;
        grid[7][1] = 102;
        grid[8][8] = 103;
        var input = snakeSightRaysDTO.getInput(new Snake(grid, 4, 4, Direction.UP, 1));

        assertThat(input[0], is(closeTo(-0.2, 0.1)));
        assertThat(input[1], is(closeTo(0.2, 0.1)));
        assertThat(input[2], is(closeTo(0.0, 0.1)));
        assertThat(input[3], is(closeTo(-0.2, 0.1)));

        assertThat(input[4], is(closeTo(-0.2, 0.1)));
        assertThat(input[5], is(closeTo(-0.2, 0.1)));
        assertThat(input[6], is(closeTo(-0.33, 0.1)));
        assertThat(input[7], is(closeTo(0.5, 0.1)));
    }

    @Test
    void wrapAroundCoordinates_isCorrect() {
        initGrid();
        snakeSightRaysDTO = new SnakeSightRaysDTO(grid);
        assertThat(snakeSightRaysDTO.wrapAroundCoordinates(4, 5), is(4));
        assertThat(snakeSightRaysDTO.wrapAroundCoordinates(5, 5), is(0));
        assertThat(snakeSightRaysDTO.wrapAroundCoordinates(6, 5), is(1));
        assertThat(snakeSightRaysDTO.wrapAroundCoordinates(0, 5), is(0));
        assertThat(snakeSightRaysDTO.wrapAroundCoordinates(-1, 5), is(4));
        assertThat(snakeSightRaysDTO.wrapAroundCoordinates(-2, 5), is(3));
    }

    private void initGrid() {
        grid = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (Settings.HAS_WALL && (row == 0 || row == SIZE - 1 || column == 0 || column == SIZE - 1))
                    grid[row][column] = SnakeMap.WALL.value;
            }
        }
        snakeSightRaysDTO = new SnakeSightRaysDTO(grid);
    }
}
