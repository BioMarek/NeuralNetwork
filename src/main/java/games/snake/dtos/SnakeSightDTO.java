package games.snake.dtos;

import games.freeEvolution.FESnake;
import games.snake.Snake;

public interface SnakeSightDTO {
    double[] getInput(Snake snake);
    double[] getInput(FESnake snake);
}
