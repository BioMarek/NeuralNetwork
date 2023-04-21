package games.snake;

import utils.Direction;
import utils.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static games.snake.SnakeMap.BODY;
import static games.snake.SnakeMap.HEAD;

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
        return grid[row][column] >= BODY.value && (grid[row][column] != name + BODY.value && grid[row][column] != name + HEAD.value);
    }

    public int uniqueTilesOccupied() {
        Set<Pair<Integer>> usedCoordinates = new HashSet<>();
        bodyParts.forEach(bodyPart -> usedCoordinates.add(new Pair<>(bodyPart.row, bodyPart.column)));
        return usedCoordinates.size();
    }
}
