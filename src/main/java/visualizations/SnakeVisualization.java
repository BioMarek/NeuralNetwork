package visualizations;

import games.snake.dtos.SavedGameDTO;
import interfaces.GridVisualization;
import utils.Colors;
import utils.Settings;

import java.awt.Graphics2D;

public class SnakeVisualization implements GridVisualization {
    private Graphics2D graphics;
    private SavedGameDTO savedGameDTO;
    private int currentFrame = 0;
    private int size;
    private int squareSizePixels;

    public SnakeVisualization(SavedGameDTO savedGameDTO) {
        this.savedGameDTO = savedGameDTO;
        this.size = savedGameDTO.grid.get(0).length;
        this.squareSizePixels = Settings.backgroundWidth / size;
    }

    @Override
    public void createNextFrame() {
        currentFrame++;
    }

    @Override
    public void drawPresentation(Graphics2D graphics) {
        this.graphics = graphics;
        setBackground();
        drawGrid();
    }

    public void setBackground() {
        graphics.setColor(Colors.BACKGROUND.getColor());
        graphics.fillRect(0, 0, Settings.backgroundWidth, Settings.backgroundHeight);
    }

    public void drawGrid() {
        int[][] currentGrid = savedGameDTO.grid.get(currentFrame);
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (currentGrid[row][column] == 0)
                    continue;
                Colors.setColor(graphics, numberToColor(currentGrid[row][column]));
                graphics.fillRect(column * squareSizePixels, row * squareSizePixels, squareSizePixels, squareSizePixels);
            }
        }
    }

    public int numberToColor(int num) {
        // TODO refactor together with colors
        if (num == 1)
            return 0;
        if (num == 2)
            return 18;
        if (num >= 200)
            return num - 200;
        if (num >= 100)
            return num - 100;
        throw new RuntimeException("wrong number in numberToColor");
    }
}
