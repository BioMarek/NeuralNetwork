package games.snake;

import TestUtils.TestNeuralNetwork;
import interfaces.NeuralNetwork;
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
        Settings.numOfApples = 2;
        Settings.gridSize = 4;
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
        Settings.numOfPlayers = 0;
        Settings.numOfApples = 0;

        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        snakeMultiplayerGame.grid[1][1] = 100;
        snakeMultiplayerGame.grid[1][2] = 101;
        snakeMultiplayerGame.grid[2][1] = 2;
        snakeMultiplayerGame.printSnakeGame();

        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(2, 2, Direction.UP, 1), 3, 3), is(true));
        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(2, 2, Direction.UP, 1), 1, 1), is(true));
        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(2, 2, Direction.UP, 1), 1, 2), is(false));
        assertThat(snakeMultiplayerGame.snakeCollision(new Snake(2, 2, Direction.UP, 1), 2, 1), is(false));

        Settings.numOfPlayers = 2;
        Settings.numOfApples = 2;
    }

    @Test
    void moveSnakeByOne_works() {
        Settings.numOfPlayers = 0;
        Settings.numOfApples = 0;

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

        Settings.numOfPlayers = 2;
        Settings.numOfApples = 2;
    }

    @Test
    void moveSnake_works() {
        Settings.numOfPlayers = 0;
        Settings.numOfApples = 0;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(1, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.placeSnake(snake);
        snakeMultiplayerGame.moveSnake(snake, 0, 1);

        assertThat(snakeMultiplayerGame.grid[1][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[1][2], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][2], is(not(101)));

        Settings.numOfPlayers = 2;
        Settings.numOfApples = 2;
    }

    @Test
    void moveSnakeToDirection_collision() {
        Settings.numOfPlayers = 0;
        Settings.numOfApples = 0;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(1, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.placeSnake(snake);
        snakeMultiplayerGame.moveSnakeToDirection(snake, Direction.LEFT);

        assertThat(snakeMultiplayerGame.grid[1][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][1], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[1][2], is(not(101)));
        assertThat(snakeMultiplayerGame.grid[2][2], is(not(101)));

        Settings.numOfPlayers = 2;
        Settings.numOfApples = 2;
    }

    @Test
    void moveSnakeToDirection_foodEaten() {
        Settings.numOfPlayers = 0;
        Settings.numOfApples = 0;
        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(1, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.placeSnake(snake);
        snakeMultiplayerGame.grid[1][2] = 2;
        snakeMultiplayerGame.moveSnakeToDirection(snake, Direction.RIGHT);

        assertThat(snakeMultiplayerGame.grid[1][2], is(not(2)));

        Settings.numOfPlayers = 2;
        Settings.numOfApples = 2;
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
        Settings.numOfPlayers = 0;
        Settings.numOfApples = 0;

        var snakeMultiplayerGame = new SnakeGameMultiplayer();
        var snake = new Snake(1, 1, Direction.DOWN, 1);
        snakeMultiplayerGame.snakes.add(snake);
        snakeMultiplayerGame.placeSnake(snake);

        NeuralNetwork network = new TestNeuralNetwork();
        var networks = List.of(network);
        snakeMultiplayerGame.play(networks, 1);

        assertThat(snakeMultiplayerGame.grid[1][1], is(0));
        assertThat(snake.snakeScore, is(Settings.deathPenalty));

        Settings.numOfPlayers = 2;
        Settings.numOfApples = 2;
    }

    @Test
    void play_worksWithFood() {
        Settings.numOfPlayers = 0;
        Settings.numOfApples = 0;

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

        Settings.numOfPlayers = 2;
        Settings.numOfApples = 2;
    }


    @AfterEach
    void cleanup() {
        Settings.numOfApples = 2;
        Settings.gridSize = 20;
    }
}
