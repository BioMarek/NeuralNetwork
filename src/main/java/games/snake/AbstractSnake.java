package games.snake;

import utils.Direction;
import utils.Settings;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSnake {
    protected int[][] grid;
    public List<BodyPart> bodyParts = new ArrayList<>();
    public Direction lastDirection;
    public int color;
    public int snakeScore;
    public int stepsMoved;

    /**
     * Checks whether on particular row and column of grid is or is not Snake different from this one.
     *
     * @param row    of grid where to check
     * @param column of grid where to check
     * @return true if there is different snake false otherwise
     */
    public boolean isSnakeCollision(int row, int column) {
        if (Settings.SELF_COLLISION)
            return grid[row][column] >= SnakeMap.BODY.value;
        else
            return grid[row][column] >= SnakeMap.BODY.value && (grid[row][column] != color + SnakeMap.BODY.value && grid[row][column] != color + SnakeMap.HEAD.value);
    }

    /**
     * Removes {@link Snake} from grid. Grid squares that were occupied by snake {@link BodyPart}s will get new number
     * based on whether we want to leave food in place of dead snake or just remove it.
     *
     * @param leaveCorpse whether to leave food on dead {@link BodyPart}s
     * @return how much food was placed on the grid
     */
    public int removeSnake(boolean leaveCorpse) {
        int newFoodPlaced = 0;
        for (BodyPart bodyPart : bodyParts) {
            grid[bodyPart.row][bodyPart.column] = SnakeMap.EMPTY.value;
        }
        if (leaveCorpse)
            for (BodyPart bodyPart : bodyParts) {
                if (grid[bodyPart.row][bodyPart.column] != SnakeMap.FOOD.value) {
                    grid[bodyPart.row][bodyPart.column] = SnakeMap.FOOD.value;
                    newFoodPlaced++;
                }
            }
        return newFoodPlaced;
    }

    /**
     * Resets Snake to default configuration.
     *
     * @param row       on which Snake {@link BodyPart}s will be created
     * @param column    on which Snake {@link BodyPart}s will be created
     * @param direction lastDirection of snake
     * @param length    how many {@link BodyPart}s Snake should have
     */
    public void resetSnake(int row, int column, Direction direction, int length) {
        assert length >= 1;
        bodyParts = new ArrayList<>();
        bodyParts.add(new BodyPart(true, row, column));
        for (int i = 1; i < length; i++) {
            bodyParts.add(new BodyPart(false, row, column));
        }
        this.lastDirection = direction;
    }

    /**
     * Places Snakes {@link BodyPart}s onto grid.
     */
    public void placeSnake() {
        for (int j = bodyParts.size() - 1; j >= 0; j--) { // head will be always on top of other bodyparts
            var bodyPart = bodyParts.get(j);
            if (bodyPart.isHead)
                grid[bodyPart.row][bodyPart.column] = color + SnakeMap.HEAD.value;
            else
                grid[bodyPart.row][bodyPart.column] = color + SnakeMap.BODY.value;
        }
    }

    /**
     * @return size of snake i.e. number of {@link BodyPart}s
     */
    public int size() {
        return bodyParts.size();
    }
}
