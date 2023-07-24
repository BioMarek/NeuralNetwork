package games.snake;

import org.junit.jupiter.api.Test;
import utils.Direction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SnakeTest {

    @Test
    void snakeIsInitializedCorrectly() {
        var snake = new Snake(null, 1, 2, Direction.UP, 1);
        assertThat(snake.bodyParts.get(0).isHead, is(true));
        assertThat(snake.bodyParts.get(1).isHead, is(false));
        assertThat(snake.bodyParts.get(2).isHead, is(false));

        assertThat(snake.bodyParts.get(0).row, is(1));
        assertThat(snake.bodyParts.get(1).row, is(1));
        assertThat(snake.bodyParts.get(2).row, is(1));

        assertThat(snake.bodyParts.get(0).column, is(2));
        assertThat(snake.bodyParts.get(1).column, is(2));
        assertThat(snake.bodyParts.get(2).column, is(2));

        assertThat(snake.lastDirection, is(Direction.UP));
        assertThat(snake.lastDirection, is(Direction.UP));
        assertThat(snake.lastDirection, is(Direction.UP));

        assertThat(snake.color, is(1));
    }
}
