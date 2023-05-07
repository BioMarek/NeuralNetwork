package visualizations.graphic;

import games.snake.SnakeMap;
import games.snake.savegame.SavedGameDTO;
import utils.Colors;
import utils.Settings;

import java.awt.Graphics2D;

public class SnakeVisualization implements GridVisualization {
    private Graphics2D graphics;
    private final SnakeLegend snakeLegend;
    private final SavedGameDTO savedGameDTO;
    private int currentFrame = 0;
    private final int columns;
    private final int rows;
    private final int squareSizePixels;
    private final int maxFrames;

    public SnakeVisualization(SavedGameDTO savedGameDTO) {
        this.savedGameDTO = savedGameDTO;
        this.maxFrames = savedGameDTO.grid.size();
        this.snakeLegend = new SnakeLegend(graphics, savedGameDTO);
        rows = savedGameDTO.rows;
        columns = savedGameDTO.columns;
        this.squareSizePixels = 20;
    }

    @Override
    public void createNextFrame() {
        if (currentFrame++ > maxFrames)
            System.exit(0);
    }

    @Override
    public void drawPresentation(Graphics2D graphics) {
        if (currentFrame < Settings.MAX_NUM_OF_MOVES) {
            this.graphics = graphics;
            snakeLegend.graphics = graphics;
            setBackground();
            drawGrid();
            snakeLegend.drawLegend(currentFrame);
        }
    }

    public void setBackground() {
        graphics.setColor(Colors.BACKGROUND.getColor());
        graphics.fillRect(0, 0, Settings.BACKGROUND_WIDTH, Settings.BACKGROUND_HEIGHT);
    }

    public void drawGrid() {
        int[][] currentGrid = savedGameDTO.grid.get(currentFrame);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (currentGrid[row][column] == 0)
                    continue;
                numberToColor(currentGrid[row][column]);
                graphics.fillRect(column * squareSizePixels, row * squareSizePixels, squareSizePixels, squareSizePixels);
            }
        }
    }

    public void numberToColor(int num) {
        if (num >= SnakeMap.HEAD.value)
            Colors.setColor(graphics, num - SnakeMap.HEAD.value + 3, 255); // +2 because 1 is wall and 2 is food
        else if (num >= SnakeMap.BODY.value)
            Colors.setColor(graphics, num - SnakeMap.BODY.value + 3, 150);
        else
            Colors.setColor(graphics, num, 255);
    }
}
