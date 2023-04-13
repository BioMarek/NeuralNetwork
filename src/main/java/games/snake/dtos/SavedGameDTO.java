package games.snake.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to save information about game so it can be visualized later.
 */
public class SavedGameDTO {
    public List<int[][]> grid = new ArrayList<>();
    public List<Integer> scores;
}
