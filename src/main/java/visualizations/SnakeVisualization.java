package visualizations;

import games.snake.savegame.SavedGameDTO;
import interfaces.GridVisualization;
import utils.Colors;

import java.awt.Graphics2D;

import static games.snake.SnakeMap.BODY;
import static games.snake.SnakeMap.HEAD;
import static utils.Settings.GRID_WIDTH;
import static utils.Settings.GRID_HEIGHT;
import static utils.Settings.MAX_NUM_OF_MOVES;

public class SnakeVisualization implements GridVisualization {
    private Graphics2D graphics;
    private final SavedGameDTO savedGameDTO;
    private int currentFrame = 0;
    private final int width;
    private final int height;
    private final int squareSizePixels;

    public SnakeVisualization(SavedGameDTO savedGameDTO) {
        this.savedGameDTO = savedGameDTO;
        height = savedGameDTO.height;
        width = savedGameDTO.width;
        this.squareSizePixels = 20;
    }

    @Override
    public void createNextFrame() {
        currentFrame++;
    }

    @Override
    public void drawPresentation(Graphics2D graphics) {
        if (currentFrame < MAX_NUM_OF_MOVES) {
            this.graphics = graphics;
            setBackground();
            drawGrid();
        }
    }

    public void setBackground() {
        graphics.setColor(Colors.BACKGROUND.getColor());
        graphics.fillRect(0, 0, GRID_WIDTH, GRID_HEIGHT);
    }

    public void drawGrid() {
        int[][] currentGrid = savedGameDTO.grid.get(currentFrame);
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if (currentGrid[row][column] == 0)
                    continue;
                numberToColor(currentGrid[row][column]);
                graphics.fillRect(column * squareSizePixels, row * squareSizePixels, squareSizePixels, squareSizePixels);
            }
        }
    }

    public void numberToColor(int num) {
        if (num >= HEAD.value)
            Colors.setColor(graphics, num - HEAD.value + 3, 255); // +2 because 1 is wall and 2 is food
        else if (num >= BODY.value)
            Colors.setColor(graphics, num - BODY.value + 3, 150);
        else
            Colors.setColor(graphics, num, 255);
    }
}
