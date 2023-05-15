package games.snake;

import TestUtils.TestNeuralNetwork;
import neat.phenotype.NeuralNetwork;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Settings;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class SnakeGameMultiplayerTest {

    @BeforeEach
    void setup() {
        Settings.MAX_NUM_OF_FOOD = 2;
        Settings.GRID_ROWS = 4;
        Settings.GRID_COLUMNS = 4;
        Settings.PIXELS_PER_SQUARE = 1;
        Settings.LEAVE_CORPSE = false;
        Settings.HAS_WALL = true;
        Settings.SELF_COLLISION = false;
    }

    @Test
    void initializationIsSuccessful() {
        var snakeMultiplayerGame = new SnakeGameMultiplayer();

        assertThat(snakeMultiplayerGame.snakes.size(), is(2));
        assertThat(snakeMultiplayerGame.grid[1][1], is(not(0)));
        assertThat(snakeMultiplayerGame.grid[1][2], is(not(0)));
        assertThat(snakeMultiplayerGame.grid[2][1], is(not(0)));
        assertThat(snakeMultiplayerGame.grid[2][2], is(not(0)));
    }

    @Test
    void snakeCollision_works() {
        Settings.NUM_OF_PLAYERS = 0;
        Settings.MAX_NUM_OF_FOOD = 0;

        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        snakeMultiplayerGame.grid[1][1] = 100;
        snakeMultiplayerGame.grid[1][2] = 101;
        snakeMultiplayerGame.grid[2][1] = 2;

        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(snakeMultiplayerGame.grid, 2, 2, Direction.UP, 1), 3, 3), is(true));
        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(snakeMultiplayerGame.grid, 2, 2, Direction.UP, 1), 1, 1), is(true));
        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(snakeMultiplayerGame.grid, 2, 2, Direction.UP, 1), 1, 2), is(false));
        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(snakeMultiplayerGame.grid, 2, 2, Direction.UP, 1), 2, 1), is(false));

        Settings.NUM_OF_PLAYERS = 2;
        Settings.MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void moveSnakeByOne_works() {
        Settings.NUM_OF_PLAYERS = 0;
        Settings.MAX_NUM_OF_FOOD = 0;

        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(snakeMultiplayerGame.grid, 1, 1, Direction.DOWN, 1);
        snake.placeSnake();

        snakeMultiplayerGame.moveSnakeByOne(snake, 2, 1);
        assertThat(snakeMultiplayerGame.grid[1][1], is(101));
        assertThat(snakeMultiplayerGame.grid[2][1], is(201));

        snakeMultiplayerGame.moveSnakeByOne(snake, 2, 2);
        assertThat(snakeMultiplayerGame.grid[1][1], is(101));
        assertThat(snakeMultiplayerGame.grid[2][1], is(101));
        assertThat(snakeMultiplayerGame.grid[2][2], is(201));

        snakeMultiplayerGame.moveSnakeByOne(snake, 1, 2);
        assertThat(snakeMultiplayerGame.grid[1][1], is(0));
        assertThat(snakeMultiplayerGame.grid[2][1], is(101));
        assertThat(snakeMultiplayerGame.grid[2][2], is(101));
        assertThat(snakeMultiplayerGame.grid[1][2], is(201));

        Settings.NUM_OF_PLAYERS = 2;
        Settings.MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void moveSnake_works() {
        Settings.NUM_OF_PLAYERS = 0;
        Settings.MAX_NUM_OF_FOOD = 0;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(snakeMultiplayerGame.grid, 1, 1, Direction.DOWN, 1);
        snake.placeSnake();
        snakeMultiplayerGame.moveSnake(snake, 0, 1);

        assertThat(snakeMultiplayerGame.grid[1][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[1][2], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][2], is(not(101)));

        Settings.NUM_OF_PLAYERS = 2;
        Settings.MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void moveSnakeToDirection_collision() {
        Settings.NUM_OF_PLAYERS = 0;
        Settings.MAX_NUM_OF_FOOD = 0;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(snakeMultiplayerGame.grid, 1, 1, Direction.DOWN, 1);
        snake.placeSnake();
        snakeMultiplayerGame.moveSnakeToDirection(snake, Direction.LEFT);

        assertThat(snakeMultiplayerGame.grid[1][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[1][2], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][2], is(not(101)));

        Settings.NUM_OF_PLAYERS = 2;
        Settings.MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void moveSnakeToDirection_foodEaten() {
        Settings.NUM_OF_PLAYERS = 0;
        Settings.MAX_NUM_OF_FOOD = 0;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(snakeMultiplayerGame.grid, 1, 1, Direction.DOWN, 1);
        snake.placeSnake();
        snakeMultiplayerGame.grid[1][2] = 2;
        snakeMultiplayerGame.moveSnakeToDirection(snake, Direction.RIGHT);

        assertThat(snakeMultiplayerGame.grid[1][2], is(not(2)));

        Settings.NUM_OF_PLAYERS = 2;
        Settings.MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void maxValueIndex_returnsCorrectValue() {
        var snakeMultiplayerGame = new SnakeGameMultiplayer();

        assertThat(snakeMultiplayerGame.maxValueIndex(new double[]{1.0, 0.5, -0.5, -1.0}), is(0));
        assertThat(snakeMultiplayerGame.maxValueIndex(new double[]{-1.0, 0.5, -0.5, -1.0}), is(1));
        assertThat(snakeMultiplayerGame.maxValueIndex(new double[]{-1.0, -0.5, -0.5, -1.0}), is(1));
    }

    @Test
    void outputToDirection_returnsCorrectDirection() {
        var snakeMultiplayerGame = new SnakeGameMultiplayer();

        assertThat(snakeMultiplayerGame.outputToDirection(new double[]{1.0, 0.5, -0.5, -1.0}), is(Direction.UP));
        assertThat(snakeMultiplayerGame.outputToDirection(new double[]{-1.0, 0.5, -0.5, -1.0}), is(Direction.RIGHT));
        assertThat(snakeMultiplayerGame.outputToDirection(new double[]{-1.0, -0.5, 0.5, -1.0}), is(Direction.DOWN));
        assertThat(snakeMultiplayerGame.outputToDirection(new double[]{-1.0, -0.5, -0.5, 1.0}), is(Direction.LEFT));
    }

    @Test
    void play_worksWithCollisionAndSnakeRestart() {
        Settings.NUM_OF_PLAYERS = 0;
        Settings.MAX_NUM_OF_FOOD = 0;

        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(snakeMultiplayerGame.grid, 1, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.snakes.add(snake);
        snake.placeSnake();

        NeuralNetwork network = new TestNeuralNetwork();
        var networks = List.of(network);
        snakeMultiplayerGame.play(networks, 1);

        assertThat(snakeMultiplayerGame.grid[1][1], is(0));
        assertThat(snake.snakeScore, is(Settings.DEATH_PENALTY));

        Settings.NUM_OF_PLAYERS = 2;
        Settings.MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void play_worksWithFood() {
        Settings.NUM_OF_PLAYERS = 0;
        Settings.MAX_NUM_OF_FOOD = 0;

        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(snakeMultiplayerGame.grid, 2, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.grid[1][1] = SnakeMap.FOOD.value;
        snakeMultiplayerGame.snakes.add(snake);
        snake.placeSnake();

        NeuralNetwork network = new TestNeuralNetwork();
        var networks = List.of(network);
        snakeMultiplayerGame.play(networks, 1);

        assertThat(snakeMultiplayerGame.grid[1][1], is(201));
        assertThat(snakeMultiplayerGame.grid[2][1], is(101));
        assertThat(snakeMultiplayerGame.grid[1][2] + snakeMultiplayerGame.grid[2][2], is(1)); // one new food in two grid squares
        assertThat(snake.snakeScore, is(1));

        Settings.NUM_OF_PLAYERS = 2;
        Settings.MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void play_deadSnakeLeavesCorpse() {
        Settings.NUM_OF_PLAYERS = 0;
        Settings.MAX_NUM_OF_FOOD = 0;
        Settings.LEAVE_CORPSE = true;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(snakeMultiplayerGame.grid, 2, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.snakes.add(snake);
        snake.placeSnake();

        assertThat(snakeMultiplayerGame.numOfFood, is(0));

        NeuralNetwork network = new TestNeuralNetwork();
        var networks = List.of(network);
        snakeMultiplayerGame.play(networks, 2);

        assertThat(snakeMultiplayerGame.numOfFood, is(2));

        Settings.NUM_OF_PLAYERS = 2;
        Settings.MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void wrapAroundCoordinates_works() {
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        assertThat(snakeMultiplayerGame.wrapAroundCoordinates(4, 5), is(4));
        assertThat(snakeMultiplayerGame.wrapAroundCoordinates(5, 5), is(0));
        assertThat(snakeMultiplayerGame.wrapAroundCoordinates(0, 5), is(0));
        assertThat(snakeMultiplayerGame.wrapAroundCoordinates(-1, 5), is(4));
    }

    @Test
    void isSnakeCollision_works() {
        Settings.NUM_OF_PLAYERS = 0;
        Settings.MAX_NUM_OF_FOOD = 0;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(snakeMultiplayerGame.grid, 2, 1, Direction.NONE, 1);
        snakeMultiplayerGame.snakes.add(snake);
        snake.placeSnake();
        snakeMultiplayerGame.moveSnakeToDirection(snake, Direction.UP);
        snakeMultiplayerGame.grid[1][2] = 202;

        assertThat(snake.isSnakeCollision(2, 1), is(false));
        assertThat(snake.isSnakeCollision(1, 2), is(true));
        Settings.SELF_COLLISION = true;
        assertThat(snake.isSnakeCollision(2, 1), is(true));
        Settings.SELF_COLLISION = false;
    }

    @AfterEach
    void cleanup() {
        Settings.LEAVE_CORPSE = true;
        Settings.MAX_NUM_OF_FOOD = 2;
        Settings.GRID_ROWS = 20;
        Settings.GRID_COLUMNS = 20;
        Settings.PIXELS_PER_SQUARE = 20;
        Settings.HAS_WALL = false;
        Settings.SELF_COLLISION = false;
    }
}
