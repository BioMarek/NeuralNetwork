package games.snake.freeEvolution;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Settings;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class FEGameTest {

    @BeforeEach
    void setup() {
        Settings.MAX_NUM_OF_FOOD = 2;
        Settings.GRID_ROW_PIXELS = 4;
        Settings.GRID_COLUMN_PIXELS = 4;
        Settings.PIXELS_PER_SQUARE = 1;
        Settings.LEAVE_CORPSE = false;
        Settings.HAS_WALL = true;
        Settings.SELF_COLLISION = false;
    }

    @Test
    void moveSnakeToDirection_collision() {
        Settings.NUM_OF_PLAYERS = 0;
        Settings.MAX_NUM_OF_FOOD = 0;

        var feGame = new FEGame(8, 5);
        var snake = new FESnake(feGame.grid, 1, 1, Direction.DOWN, 1, 2);
        snake.placeSnake();
        feGame.moveSnakeToDirection(snake, Direction.LEFT);

        assertThat(feGame.grid[1][1], is(not(101)));
        assertThat(feGame.grid[2][1], is(not(101)));
        assertThat(feGame.grid[1][2], is(not(101)));
        assertThat(feGame.grid[2][2], is(not(101)));

        assertThat(feGame.grid[1][1], is(not(201)));
        assertThat(feGame.grid[2][1], is(not(201)));
        assertThat(feGame.grid[1][2], is(not(201)));
        assertThat(feGame.grid[2][2], is(not(201)));

        Settings.NUM_OF_PLAYERS = 2;
        Settings.MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void moveSnakeToDirection_length1CanGoOppositeDirection() {
        Settings.NUM_OF_PLAYERS = 0;
        Settings.MAX_NUM_OF_FOOD = 0;

        var feGame = new FEGame(8, 5);
        var snake = new FESnake(feGame.grid, 1, 1, Direction.UP, 1, 1);
        snake.placeSnake();
        feGame.moveSnakeToDirection(snake, Direction.DOWN);

        assertThat(feGame.grid[1][1], is(not(101)));
        assertThat(feGame.grid[2][1], is(201));
        assertThat(feGame.grid[1][2], is(not(101)));
        assertThat(feGame.grid[2][2], is(not(101)));

        Settings.NUM_OF_PLAYERS = 2;
        Settings.MAX_NUM_OF_FOOD = 2;
    }

    @AfterEach
    void cleanup() {
        Settings.LEAVE_CORPSE = true;
        Settings.MAX_NUM_OF_FOOD = 2;
        Settings.GRID_ROW_PIXELS = 20;
        Settings.GRID_COLUMN_PIXELS = 20;
        Settings.PIXELS_PER_SQUARE = 20;
        Settings.HAS_WALL = false;
        Settings.SELF_COLLISION = false;
    }
}
