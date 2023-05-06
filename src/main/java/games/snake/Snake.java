package games.snake;

import utils.Direction;
import utils.Pair;
import utils.Settings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static games.snake.SnakeGameMultiplayer.randomFreeCoordinate;


/**
 * Initializes snake, all body parts start on same position
 */
public class Snake {
    public List<BodyPart> bodyParts = new ArrayList<>();
    public Direction lastDirection;
    public int name;
    public int snakeScore;
    public int stepsMoved;


    public Snake(int row, int column, Direction direction, int name) {
        resetSnake(row, column, direction);
        this.name = name;
        this.stepsMoved = 0;
    }

    public void resetSnake(int row, int column, Direction direction) {
        bodyParts = new ArrayList<>();
        bodyParts.add(new BodyPart(true, row, column));
        bodyParts.add(new BodyPart(false, row, column));
        bodyParts.add(new BodyPart(false, row, column));
        this.lastDirection = direction;
    }

    public boolean isAnotherSnake(int[][] grid, int row, int column) {
        return grid[row][column] >= SnakeMap.BODY.value && (grid[row][column] != name + SnakeMap.BODY.value && grid[row][column] != name + SnakeMap.HEAD.value);
    }

    public int uniqueTilesOccupied() {
        Set<Pair<Integer>> usedCoordinates = new HashSet<>();
        bodyParts.forEach(bodyPart -> usedCoordinates.add(new Pair<>(bodyPart.row, bodyPart.column)));
        return usedCoordinates.size();
    }

    public void reduceSnakeByOne(int[][] grid, SnakeMap snakeMap) {
        removeSnake(grid, snakeMap);
        if (bodyParts.size() == 1) {
            var coordinates = randomFreeCoordinate(grid);
            resetSnake(coordinates.getFirst(), coordinates.getSecond(), Direction.randomDirection());
            snakeScore += Settings.DEATH_PENALTY;
        } else {
            bodyParts.remove(bodyParts.size() - 1);
        }
        placeSnake(grid);
    }

    /**
     * Removes {@link Snake} from grid. Grid squares that were occupied by snake {@link BodyPart}s will get new number
     * based on whether we want to leave food in place of dead snake or just remove it.
     *
     * @param grid     the snake is on
     * @param snakeMap value to place on grid squares where snake bodyparts were
     */
    public void removeSnake(int[][] grid, SnakeMap snakeMap) {
        for (BodyPart bodyPart : bodyParts) {
            grid[bodyPart.row][bodyPart.column] = snakeMap.value;
        }
    }

    protected void placeSnake(int[][] grid) {
        for (int j = bodyParts.size() - 1; j >= 0; j--) { // head will be always on top of other bodyparts
            var bodyPart = bodyParts.get(j);
            if (bodyPart.isHead)
                grid[bodyPart.row][bodyPart.column] = name + SnakeMap.HEAD.value;
            else
                grid[bodyPart.row][bodyPart.column] = name + SnakeMap.BODY.value;
        }
    }
}
