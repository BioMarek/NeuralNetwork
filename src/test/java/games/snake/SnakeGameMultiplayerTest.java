package games.snake;

import TestUtils.TestNeuralNetwork;
import interfaces.NeuralNetwork;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static utils.Settings.DEATH_PENALTY;
import static utils.Settings.GRID_SIZE;
import static utils.Settings.LEAVE_CORPSE;
import static utils.Settings.MAX_NUM_OF_FOOD;
import static utils.Settings.NUM_OF_PLAYERS;

public class SnakeGameMultiplayerTest {

    @BeforeEach
    void setup() {
        MAX_NUM_OF_FOOD = 2;
        GRID_SIZE = 4;
        LEAVE_CORPSE = false;
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
        NUM_OF_PLAYERS = 0;
        MAX_NUM_OF_FOOD = 0;

        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        snakeMultiplayerGame.grid[1][1] = 100;
        snakeMultiplayerGame.grid[1][2] = 101;
        snakeMultiplayerGame.grid[2][1] = 2;

        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(2, 2, Direction.UP, 1), 3, 3), is(true));
        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(2, 2, Direction.UP, 1), 1, 1), is(true));
        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(2, 2, Direction.UP, 1), 1, 2), is(false));
        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(2, 2, Direction.UP, 1), 2, 1), is(false));

        NUM_OF_PLAYERS = 2;
        MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void moveSnakeByOne_works() {
        NUM_OF_PLAYERS = 0;
        MAX_NUM_OF_FOOD = 0;

        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(1, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.placeSnake(snake);

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

        NUM_OF_PLAYERS = 2;
        MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void moveSnake_works() {
        NUM_OF_PLAYERS = 0;
        MAX_NUM_OF_FOOD = 0;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(1, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.placeSnake(snake);
        snakeMultiplayerGame.moveSnake(snake, 0, 1);

        assertThat(snakeMultiplayerGame.grid[1][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[1][2], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][2], is(not(101)));

        NUM_OF_PLAYERS = 2;
        MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void moveSnakeToDirection_collision() {
        NUM_OF_PLAYERS = 0;
        MAX_NUM_OF_FOOD = 0;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(1, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.placeSnake(snake);
        snakeMultiplayerGame.moveSnakeToDirection(snake, Direction.LEFT);

        assertThat(snakeMultiplayerGame.grid[1][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[1][2], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][2], is(not(101)));

        NUM_OF_PLAYERS = 2;
        MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void moveSnakeToDirection_foodEaten() {
        NUM_OF_PLAYERS = 0;
        MAX_NUM_OF_FOOD = 0;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(1, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.placeSnake(snake);
        snakeMultiplayerGame.grid[1][2] = 2;
        snakeMultiplayerGame.moveSnakeToDirection(snake, Direction.RIGHT);

        assertThat(snakeMultiplayerGame.grid[1][2], is(not(2)));

        NUM_OF_PLAYERS = 2;
        MAX_NUM_OF_FOOD = 2;
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
        NUM_OF_PLAYERS = 0;
        MAX_NUM_OF_FOOD = 0;

        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(1, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.snakes.add(snake);
        snakeMultiplayerGame.placeSnake(snake);

        NeuralNetwork network = new TestNeuralNetwork();
        var networks = List.of(network);
        snakeMultiplayerGame.play(networks, 1);

        assertThat(snakeMultiplayerGame.grid[1][1], is(0));
        assertThat(snake.snakeScore, is(DEATH_PENALTY));

        NUM_OF_PLAYERS = 2;
        MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void play_worksWithFood() {
        NUM_OF_PLAYERS = 0;
        MAX_NUM_OF_FOOD = 0;

        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(2, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.grid[1][1] = SnakeMap.FOOD.value;
        snakeMultiplayerGame.snakes.add(snake);
        snakeMultiplayerGame.placeSnake(snake);

        NeuralNetwork network = new TestNeuralNetwork();
        var networks = List.of(network);
        snakeMultiplayerGame.play(networks, 1);

        assertThat(snakeMultiplayerGame.grid[1][1], is(201));
        assertThat(snakeMultiplayerGame.grid[2][1], is(101));
        assertThat(snakeMultiplayerGame.grid[1][2] + snakeMultiplayerGame.grid[2][2], is(2)); // one new food in two grid squares
        assertThat(snake.snakeScore, is(1));

        NUM_OF_PLAYERS = 2;
        MAX_NUM_OF_FOOD = 2;
    }

    @Test
    void play_deadSnakeLeavesCorpse() {
        NUM_OF_PLAYERS = 0;
        MAX_NUM_OF_FOOD = 0;
        LEAVE_CORPSE = true;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(2, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.snakes.add(snake);
        snakeMultiplayerGame.placeSnake(snake);

        assertThat(snakeMultiplayerGame.numOfFood, is(0));

        NeuralNetwork network = new TestNeuralNetwork();
        var networks = List.of(network);
        snakeMultiplayerGame.play(networks, 2);

        assertThat(snakeMultiplayerGame.numOfFood, is(2));

        NUM_OF_PLAYERS = 2;
        MAX_NUM_OF_FOOD = 2;

    }


    @AfterEach
    void cleanup() {
        LEAVE_CORPSE = true;
        MAX_NUM_OF_FOOD = 2;
        GRID_SIZE = 20;
    }
}
