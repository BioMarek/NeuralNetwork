package visualizations.snakeGraphic;

import games.snake.savegame.SavedGameDTO;
import utils.Colors;
import utils.Settings;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class SnakeLegend {
    private static final String FONT_NAME = "Arial";
    private final int fontUnit = Settings.BACKGROUND_HEIGHT / 60;
    private final SavedGameDTO savedGameDTO;
    public Graphics2D graphics;

    public SnakeLegend(Graphics2D graphics, SavedGameDTO savedGameDTO) {
        this.graphics = graphics;
        this.savedGameDTO = savedGameDTO;
    }

    public void drawLegend(int step) {
        switchAntiAliasing(true);
        drawInfo(step);
        switchAntiAliasing(false);
    }

    /**
     * Displays information about snakes score and number of steps game went.
     */
    public void drawInfo(int step) {
        graphics.setColor(Colors.TEXT.getColor());
        graphics.setFont(new Font(FONT_NAME, Font.BOLD, (int) (fontUnit * 1.5)));
        graphics.drawString("Steps: " + step, Settings.GRID_COLUMNS + fontUnit * 4, fontUnit * 4);
        var scoresThisStep = savedGameDTO.scores.get(step);
        for (int i = 0; i < scoresThisStep.length; i++) {
            drawScoreInfo(step, i);
        }
    }

    private void drawScoreInfo(int step, int i) {
        drawSquare(Settings.GRID_COLUMNS + fontUnit * 4, fontUnit * 4 * i + fontUnit * 7, i);
        graphics.setColor(Colors.TEXT.getColor());
        graphics.setFont(new Font(FONT_NAME, Font.BOLD, (int) (fontUnit * 1.5)));
        graphics.drawString(String.valueOf(savedGameDTO.scores.get(step)[i]), Settings.GRID_COLUMNS + fontUnit * 8, fontUnit * 4 * i + (int) (fontUnit * 8.7));
    }

    private void drawSquare(int x, int y, int i) {
        int squareSize = Settings.BACKGROUND_HEIGHT / 25;
        int fillSizeDecrease = 2;
        int fillSquareSize = squareSize - fillSizeDecrease * 2;

        graphics.fillRect(x, y, squareSize, squareSize);
        graphics.setColor(Colors.getColor(i + 3, 200)); // +3 because 1 is wall and 2 is food
        graphics.fillRect(x + fillSizeDecrease, y + fillSizeDecrease, fillSquareSize, fillSquareSize);
    }

    /**
     * Switches anti-aliasing on or off.
     *
     * @param isOn whether AA should be turned on
     */
    public void switchAntiAliasing(boolean isOn) {
        if (isOn) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
    }
}
