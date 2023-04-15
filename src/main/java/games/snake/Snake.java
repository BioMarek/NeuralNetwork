package games.snake;

import utils.Direction;

import java.util.ArrayList;
import java.util.List;

import static games.snake.SnakeMap.BODY_MULTIPLAYER;
import static games.snake.SnakeMap.HEAD_MULTIPLAYER;

/**
 * Initializes snake, all body parts start on same position
 */
public class Snake {
    public List<BodyPart> bodyParts = new ArrayList<>();
    public Direction lastDirection;
    public int name;
    public int snakeScore;


    public Snake(int row, int column, Direction direction, int name) {
        resetSnake(row, column, direction);
        this.name = name;
    }

    public void resetSnake(int row, int column, Direction direction) {
        bodyParts = new ArrayList<>();
        bodyParts.add(new BodyPart(true, row, column));
        bodyParts.add(new BodyPart(false, row, column));
        bodyParts.add(new BodyPart(false, row, column));
        this.lastDirection = direction;
    }

    public boolean isAnotherSnake(int[][] grid, int row, int column) {
        return grid[row][column] >= BODY_MULTIPLAYER.value && (grid[row][column] != name + BODY_MULTIPLAYER.value && grid[row][column] != name + HEAD_MULTIPLAYER.value);
    }
}
