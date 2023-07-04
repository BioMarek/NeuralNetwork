package visualizations.snakeGraphic.explanations;

import games.snake.SnakeMap;
import games.snake.savegame.SavedGameDTO;
import utils.Colors;
import utils.Settings;
import utils.Util;
import visualizations.snakeGraphic.GridVisualization;

import java.awt.Graphics2D;

public class IntroSnakeGame implements GridVisualization {
    private Graphics2D graphics;
    private final int SQUARE_PIXEL_SIZE = 40;
    private SavedGameDTO savedGameDTO;
    private final FinalScreenLetters finalScreenLetters;
    private final int[][] grid;
    private final int columns;
    private final int rows;
    private int frame = 0;
    private final int totalFrames = 30;

    public IntroSnakeGame() {
        this.rows = Settings.GRID_ROW_PIXELS / SQUARE_PIXEL_SIZE;
        this.columns = Settings.GRID_COLUMN_PIXELS / SQUARE_PIXEL_SIZE;
        this.grid = new int[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (Settings.HAS_WALL && (row == 0 || row == rows - 1 || column == 0 || column == columns - 1))
                    grid[row][column] = SnakeMap.WALL.value;
            }
        }
        finalScreenLetters = new FinalScreenLetters(grid);
        finalScreenLetters.finalScreen();
        savedGameDTO = buildMockDTO();
    }

    @Override
    public void createNextFrame() {
        System.out.println("creating frame " + frame);
        frame++;
    }

    @Override
    public void drawPresentation(Graphics2D graphics) {
        this.graphics = graphics;
        drawGrid();
    }

    @Override
    public boolean stopped() {
        return frame >= totalFrames;
    }

    @Override
    public String name() {
        return "Intro";
    }

    public void drawGrid() {
        int[][] currentGrid = savedGameDTO.grid.get(frame);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (currentGrid[row][column] == 0)
                    continue;
                numberToColor(currentGrid[row][column], calculateAppearingAlpha(0));
                if (currentGrid[row][column] == SnakeMap.FOOD.value) {
                    graphics.fillOval(column * SQUARE_PIXEL_SIZE, row * SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE);
                } else {
                    graphics.fillRect(column * SQUARE_PIXEL_SIZE, row * SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE);
                }
            }
        }
    }

    public SavedGameDTO buildMockDTO() {
        var saveGame = new SavedGameDTO();
        for (int i = 0; i <= totalFrames; i++) {
            saveGame.grid.add(Util.arrayCopy(grid));
            saveGame.scores.add(new int[]{0});
        }
        return saveGame;
    }

    public void numberToColor(int num, int alpha) {
        if (num == -1)
            Colors.setColor(graphics, Colors.wallWithAlpha(255));
        else if (num == SnakeMap.FOOD.value)
            Colors.setColor(graphics, Colors.redWithAlpha(alpha));
        else if (num >= SnakeMap.HEAD.value)
            Colors.setColor(graphics, num - SnakeMap.HEAD.value, alpha);
        else if (num >= SnakeMap.BODY.value)
            Colors.setColor(graphics, num - SnakeMap.BODY.value, 150 * alpha / 255);
    }

    public int calculateAppearingAlpha(int startAppearingFrame) {
        if (startAppearingFrame + Settings.CHANGING_SLOW_FRAMES < frame)
            return 255;
        else
            return (int) ((frame - startAppearingFrame) / 15.0 * 255);
    }

    // rows 54, columns 96

}
