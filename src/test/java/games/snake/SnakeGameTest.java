package games.snake;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SnakeGameTest {
    private final SnakeGame snakeGame = new SnakeGame(20);

    @Test
    void keyToDirection_returnsCorrectDirectionWhenLastDirectionIsUp() {
        snakeGame.lastDirection = Direction.UP;
        assertThat(snakeGame.toDirection("w"), is(Direction.UP));
        snakeGame.lastDirection = Direction.UP;
        assertThat(snakeGame.toDirection("a"), is(Direction.LEFT));
        snakeGame.lastDirection = Direction.UP;
        assertThat(snakeGame.toDirection("s"), is(Direction.UP));
        snakeGame.lastDirection = Direction.UP;
        assertThat(snakeGame.toDirection("d"), is(Direction.RIGHT));
    }

    @Test
    void keyToDirection_returnsCorrectDirectionWhenLastDirectionIsDown() {
        snakeGame.lastDirection = Direction.DOWN;
        assertThat(snakeGame.toDirection("w"), is(Direction.DOWN));
        snakeGame.lastDirection = Direction.DOWN;
        assertThat(snakeGame.toDirection("a"), is(Direction.LEFT));
        snakeGame.lastDirection = Direction.DOWN;
        assertThat(snakeGame.toDirection("s"), is(Direction.DOWN));
        snakeGame.lastDirection = Direction.DOWN;
        assertThat(snakeGame.toDirection("d"), is(Direction.RIGHT));
    }

    @Test
    void keyToDirection_returnsCorrectDirectionWhenLastDirectionIsLeft() {
        snakeGame.lastDirection = Direction.LEFT;
        assertThat(snakeGame.toDirection("w"), is(Direction.UP));
        snakeGame.lastDirection = Direction.LEFT;
        assertThat(snakeGame.toDirection("a"), is(Direction.LEFT));
        snakeGame.lastDirection = Direction.LEFT;
        assertThat(snakeGame.toDirection("s"), is(Direction.DOWN));
        snakeGame.lastDirection = Direction.LEFT;
        assertThat(snakeGame.toDirection("d"), is(Direction.LEFT));
    }

    @Test
    void keyToDirection_returnsCorrectDirectionWhenLastDirectionIsRight() {
        snakeGame.lastDirection = Direction.RIGHT;
        assertThat(snakeGame.toDirection("w"), is(Direction.UP));
        snakeGame.lastDirection = Direction.RIGHT;
        assertThat(snakeGame.toDirection("a"), is(Direction.RIGHT));
        snakeGame.lastDirection = Direction.RIGHT;
        assertThat(snakeGame.toDirection("s"), is(Direction.DOWN));
        snakeGame.lastDirection = Direction.RIGHT;
        assertThat(snakeGame.toDirection("d"), is(Direction.RIGHT));
    }

    @Test
    void snakeMapper_foodIsToTheUpperLeft() {
        snakeGame.foodRow = 8;
        snakeGame.foodColumn = 8;

        assertThat(snakeGame.snakeMapper().upDistanceToFood, is(2));
        assertThat(snakeGame.snakeMapper().downDistanceToFood, is(-20));
        assertThat(snakeGame.snakeMapper().leftDistanceToFood, is(2));
        assertThat(snakeGame.snakeMapper().rightDistanceToFood, is(-20));
    }

    @Test
    void snakeMapper_foodIsToTheUpperRight() {
        snakeGame.foodRow = 8;
        snakeGame.foodColumn = 12;

        assertThat(snakeGame.snakeMapper().upDistanceToFood, is(2));
        assertThat(snakeGame.snakeMapper().downDistanceToFood, is(-20));
        assertThat(snakeGame.snakeMapper().leftDistanceToFood, is(-20));
        assertThat(snakeGame.snakeMapper().rightDistanceToFood, is(2));
    }

    @Test
    void snakeMapper_foodIsToTheLowerLeft() {
        snakeGame.foodRow = 12;
        snakeGame.foodColumn = 8;

        assertThat(snakeGame.snakeMapper().upDistanceToFood, is(-20));
        assertThat(snakeGame.snakeMapper().downDistanceToFood, is(2));
        assertThat(snakeGame.snakeMapper().leftDistanceToFood, is(2));
        assertThat(snakeGame.snakeMapper().rightDistanceToFood, is(-20));
    }

    @Test
    void snakeMapper_foodIsToTheLowerRight() {
        snakeGame.foodRow = 12;
        snakeGame.foodColumn = 12;

        assertThat(snakeGame.snakeMapper().upDistanceToFood, is(-20));
        assertThat(snakeGame.snakeMapper().downDistanceToFood, is(2));
        assertThat(snakeGame.snakeMapper().leftDistanceToFood, is(-20));
        assertThat(snakeGame.snakeMapper().rightDistanceToFood, is(2));
    }

    @Test
    void snakeMapper_safeInStartPosition() {
        assertThat(snakeGame.snakeMapper().leftSafe, is(1));
        assertThat(snakeGame.snakeMapper().upSafe, is(1));
        assertThat(snakeGame.snakeMapper().rightSafe, is(1));
        assertThat(snakeGame.snakeMapper().downSafe, is(-1));
    }

    @Test
    void snakeMapper_safeInTopLeftCorner() {
        snakeGame.snake.get(0).row = 1;
        snakeGame.snake.get(0).column = 1;

        assertThat(snakeGame.snakeMapper().leftSafe, is(-1));
        assertThat(snakeGame.snakeMapper().upSafe, is(-1));
        assertThat(snakeGame.snakeMapper().rightSafe, is(1));
        assertThat(snakeGame.snakeMapper().downSafe, is(1));
    }

    @Test
    void snakeMapper_safeInBottomRightCorner() {
        snakeGame.snake.get(0).row = 18;
        snakeGame.snake.get(0).column = 18;

        assertThat(snakeGame.snakeMapper().leftSafe, is(1));
        assertThat(snakeGame.snakeMapper().upSafe, is(1));
        assertThat(snakeGame.snakeMapper().rightSafe, is(-1));
        assertThat(snakeGame.snakeMapper().downSafe, is(-1));
    }

    @Test
    void indexOfMaxValue_returnsCorrectDirection() {
        double[] output = new double[]{2, 1, 1, 1};
        assertThat(snakeGame.maxValueIndex(output), is(0));
        output = new double[]{1, 2, 1, 1};
        assertThat(snakeGame.maxValueIndex(output), is(1));
        output = new double[]{1, 1, 2, 1};
        assertThat(snakeGame.maxValueIndex(output), is(2));
        output = new double[]{1, 1, 1, 2};
        assertThat(snakeGame.maxValueIndex(output), is(3));
    }

    @Test
    void outputToDirection_returnsCorrectDirection() {
        double[] output = new double[]{2, 1, 1, 1};
        snakeGame.lastDirection = Direction.LEFT;
        assertThat(snakeGame.outputToDirection(output), is(Direction.UP));

        output = new double[]{1, 2, 1, 1};
        snakeGame.lastDirection = Direction.LEFT;
        assertThat(snakeGame.outputToDirection(output), is(Direction.DOWN));

        output = new double[]{1, 1, 2, 1};
        snakeGame.lastDirection = Direction.DOWN;
        assertThat(snakeGame.outputToDirection(output), is(Direction.LEFT));

        output = new double[]{1, 1, 1, 2};
        snakeGame.lastDirection = Direction.DOWN;
        assertThat(snakeGame.outputToDirection(output), is(Direction.RIGHT));
    }
}
