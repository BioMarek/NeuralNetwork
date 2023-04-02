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
}
