package games.snake.savegame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to save information about game so it can be visualized later.
 */
public class SavedGameDTO implements Serializable {
    public List<int[][]> grid = new ArrayList<>();
    public int columns;
    public int rows;
    public List<Integer> scores;
}
