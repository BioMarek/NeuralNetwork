package games.freeEvolution;

import games.snake.BodyPart;
import games.snake.Snake;
import games.snake.SnakeMap;
import neat.phenotype.NeuralNetwork;
import utils.Direction;
import utils.Settings;
import utils.Util;

import java.util.ArrayList;
import java.util.List;

public class FESnake {
    public static int names = 0;
    public FEGenotype genotype;
    protected int[][] grid;
    public List<BodyPart> bodyParts = new ArrayList<>();
    public Direction lastDirection;
    public int color;
    public int id;
    public int snakeScore;
    public int stepsMoved;


    public FESnake(int[][] grid, int row, int column, Direction direction, int id) {
        resetSnake(row, column, direction);
        this.grid = grid;
        this.stepsMoved = 0;
        this.color = id % 25;
        this.id = id;
    }

    public FESnake(int[][] grid) {
        var coordinates = Util.randomFreeCoordinate(grid);
        resetSnake(coordinates.getFirst(), coordinates.getSecond(), Direction.NONE);
        this.grid = grid;
        this.stepsMoved = 0;
        this.color = names++ % 25;
        this.id = names;
    }

    public void resetSnake(int row, int column, Direction direction) {
        bodyParts = new ArrayList<>();
        bodyParts.add(new BodyPart(true, row, column));
        bodyParts.add(new BodyPart(false, row, column));
        bodyParts.add(new BodyPart(false, row, column));
        this.lastDirection = direction;
    }

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
     * Removes one {@link BodyPart}. Used to simulate starvation
     */
    public void reduceSnakeByOne() {
        removeSnake(false);
        bodyParts.remove(bodyParts.size() - 1);
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

    public FESnake produceOffSpring() {
        removeSnake(false);
        var lastBodypart = bodyParts.get(bodyParts.size() - 1);
        var offspring = new FESnake(grid, lastBodypart.row, lastBodypart.column, Direction.opposite(lastDirection), id);
        offspring.genotype = genotype.getMutatedCopy();
        System.out.println("parent id " + id + " " + color + " offspring " + id + " " +color);
        for (int i = 0; i < Settings.OFFSPRING_COST; i++) {
            bodyParts.remove(bodyParts.size() - 1);
        }
        placeSnake();
        return offspring;
    }

    public NeuralNetwork getNeuralNetwork() {
        return genotype.createPhenotype();
    }

    public int size() {
        return bodyParts.size();
    }
}
