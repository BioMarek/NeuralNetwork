package games.snake;

import utils.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Initializes snake, all body parts start on same position
 */
public class Snake {
    public List<BodyPart> bodyParts = new ArrayList<>();
    public Direction lastDirection;
    public int snakeScore;

    public Snake(int row, int column) {
        bodyParts.add(new BodyPart(true, row, column));
        bodyParts.add(new BodyPart(false, row, column));
        bodyParts.add(new BodyPart(false, row, column));
        lastDirection = Direction.randomDirection();
    }

    public Snake(int row, int column, Direction direction) {
        bodyParts.add(new BodyPart(true, row, column));
        bodyParts.add(new BodyPart(false, row, column));
        bodyParts.add(new BodyPart(false, row, column));
        this.lastDirection = direction;
    }
}
