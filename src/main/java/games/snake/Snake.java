package games.snake;

import utils.Direction;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    protected List<BodyPart> snake = new ArrayList<>();
    public Direction lastDirection;
    public int snakeScore;

    public Snake(int row, int column) {
        snake.add(new BodyPart(true, row, column));
        snake.add(new BodyPart(false, row, column));
        snake.add(new BodyPart(false, row, column));
        lastDirection = Direction.randomDirection();
    }

    public Snake(int row, int column, Direction direction) {
        snake.add(new BodyPart(true, row, column));
        snake.add(new BodyPart(false, row, column));
        snake.add(new BodyPart(false, row, column));
        this.lastDirection = direction;
    }
}
