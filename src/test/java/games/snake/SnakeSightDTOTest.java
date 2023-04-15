package games.snake;

import games.snake.dtos.SnakeSightDTO;
import org.junit.jupiter.api.Test;
import utils.Direction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static utils.Settings.HAS_WALL;
import static utils.Settings.SNAKE_SIGHT;

public class SnakeSightDTOTest {
    private static int SIZE = 4;
    private int[][] grid;
    private SnakeSightDTO snakeSightDTO;


    @Test
    void calculateSightRay_isCorrectGridFour() {
        SNAKE_SIGHT = 4;
        SIZE = 4;
        initGrid();
        var input = snakeSightDTO.getInput_8(new Snake(1, 1, Direction.UP, 1));
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
        SNAKE_SIGHT = 4;
        SIZE = 5;
        initGrid();
        grid[3][3] = SnakeMap.FOOD.value;
        grid[2][3] = SnakeMap.FOOD.value;
        grid[1][3] = 301;
        var input = snakeSightDTO.getInput_8(new Snake(2, 2, Direction.UP, 1));
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
        SNAKE_SIGHT = 4;
        SIZE = 10;
        initGrid();
        grid[4][8] = SnakeMap.FOOD.value;
        grid[2][2] = SnakeMap.FOOD.value;
        grid[4][5] = 101;
        grid[7][1] = 102;
        grid[8][8] = 103;
        var input = snakeSightDTO.getInput_8(new Snake(4, 4, Direction.UP, 1));

        assertThat(input[0], is(closeTo(-0.2, 0.1)));
        assertThat(input[1], is(closeTo(0.2, 0.1)));
        assertThat(input[2], is(closeTo(0.0, 0.1)));
        assertThat(input[3], is(closeTo(-0.2, 0.1)));

        assertThat(input[4], is(closeTo(-0.2, 0.1)));
        assertThat(input[5], is(closeTo(-0.2, 0.1)));
        assertThat(input[6], is(closeTo(-0.33, 0.1)));
        assertThat(input[7], is(closeTo(0.5, 0.1)));
    }

    private void initGrid() {
        grid = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (HAS_WALL && (row == 0 || row == SIZE - 1 || column == 0 || column == SIZE - 1))
                    grid[row][column] = SnakeMap.WALL.value;
            }
        }
        snakeSightDTO = new SnakeSightDTO(grid);
    }
}
