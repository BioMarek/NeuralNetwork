package games.snake;

import neat.phenotype.NeuralNetwork;
import utils.Direction;
import utils.Settings;

import static utils.Util.randomFreeCoordinate;


/**
 * Initializes snake, all body parts start on same position
 */
public class Snake extends AbstractSnake {
    public NeuralNetwork neuralNetwork;

    public Snake(int[][] grid, int row, int column, Direction direction, int color) {
        resetSnake(row, column, direction, 3);
        this.grid = grid;
        this.color = color;
        this.stepsMoved = 0;
    }

    /**
     * Removes one {@link BodyPart}. Used to simulate starvation
     */
    public void reduceSnakeByOne() {
        removeSnake(false);
        if (bodyParts.size() == 1) {
            var coordinates = randomFreeCoordinate(grid);
            resetSnake(coordinates.getFirst(), coordinates.getSecond(), Direction.randomDirection(), 3);
            snakeScore += Settings.DEATH_PENALTY;
        } else {
            bodyParts.remove(bodyParts.size() - 1);
        }
        placeSnake();
    }
}
