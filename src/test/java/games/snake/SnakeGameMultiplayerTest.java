package games.snake;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Settings;

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
        snakeMultiplayerGame.grid[2][1] = 2;

        assertThat(snakeMultiplayerGame.snakeCollision(0, 0), is(true));
        assertThat(snakeMultiplayerGame.snakeCollision(1, 1), is(true));
        assertThat(snakeMultiplayerGame.snakeCollision(1, 2), is(false));
        assertThat(snakeMultiplayerGame.snakeCollision(2, 1), is(false));

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

    @AfterEach
    void cleanup() {
        Settings.numOfApples = 2;
        Settings.gridSize = 20;
    }
}
