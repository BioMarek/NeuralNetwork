package utils;

import games.snake.Snake;
import lombok.EqualsAndHashCode;

/**
 * Position of {@link Snake}.
 */
@EqualsAndHashCode
public class Position {
    public Direction direction;
    public Pair<Integer> coordinates;
}
