package games.snake.dtos;

import games.snake.BodyPart;
import games.snake.SnakeMap;
import interfaces.NeuralNetwork;

import java.util.List;

/**
 * DTO used to transfer state of snake to {@link basic_neural_network.neural_network}. DistanceToFood variables represent number of moves in
 * particular direction to food. Moving to direction that increases distance from food is designed as negative board
 * size irrespective of actual distance. Safe variables say whether move to that direction results in death (-1) or is
 * safe (1)
 */
public class SnakeBasicDTO {
    public int leftDistanceToFood;
    public int rightDistanceToFood;
    public int upDistanceToFood;
    public int downDistanceToFood;

    public int leftSafe;
    public int rightSafe;
    public int upSafe;
    public int downSafe;

    public double[] getNeuralNetworkInput() {
        double[] result = new double[8];
        result[0] = leftDistanceToFood;
        result[1] = rightDistanceToFood;
        result[2] = upDistanceToFood;
        result[3] = downDistanceToFood;

        result[4] = leftSafe;
        result[5] = rightSafe;
        result[6] = upSafe;
        result[7] = downSafe;

        return result;
    }

    /**
     * Maps current snake state to {@link SnakeBasicDTO} which is used to pass state to {@link NeuralNetwork}
     *
     * @return DTO describing state
     */
    public SnakeBasicDTO snakeMapper(List<BodyPart> bodyParts, int[][] grid, int foodRow, int foodColumn) {
        SnakeBasicDTO snakeBasicDTO = new SnakeBasicDTO();
        int wrongDirection = -1 * grid.length;

        int rowDistance = bodyParts.get(0).row - foodRow;
        if (rowDistance >= 0) {
            snakeBasicDTO.upDistanceToFood = rowDistance;
            snakeBasicDTO.downDistanceToFood = wrongDirection;
        } else {
            snakeBasicDTO.downDistanceToFood = -1 * rowDistance;
            snakeBasicDTO.upDistanceToFood = wrongDirection;
        }

        int columnDistance = bodyParts.get(0).column - foodColumn;
        if (columnDistance >= 0) {
            snakeBasicDTO.leftDistanceToFood = columnDistance;
            snakeBasicDTO.rightDistanceToFood = wrongDirection;
        } else {
            snakeBasicDTO.rightDistanceToFood = -1 * columnDistance;
            snakeBasicDTO.leftDistanceToFood = wrongDirection;
        }

        int headRow = bodyParts.get(0).row;
        int headColumn = bodyParts.get(0).column;

        snakeBasicDTO.leftSafe = (grid[headRow][headColumn - 1] == SnakeMap.WALL.value || grid[headRow][headColumn - 1] == SnakeMap.BODY.value) ? -1 : 1;
        snakeBasicDTO.rightSafe = (grid[headRow][headColumn + 1] == SnakeMap.WALL.value || grid[headRow][headColumn + 1] == SnakeMap.BODY.value) ? -1 : 1;
        snakeBasicDTO.upSafe = (grid[headRow - 1][headColumn] == SnakeMap.WALL.value || grid[headRow - 1][headColumn] == SnakeMap.BODY.value) ? -1 : 1;
        snakeBasicDTO.downSafe = (grid[headRow + 1][headColumn] == SnakeMap.WALL.value || grid[headRow + 1][headColumn] == SnakeMap.BODY.value) ? -1 : 1;
        return snakeBasicDTO;
    }
}
