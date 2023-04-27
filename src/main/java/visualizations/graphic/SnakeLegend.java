package visualizations.graphic;

import games.snake.savegame.SavedGameDTO;
import utils.Colors;
import utils.Settings;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import static utils.Settings.BACKGROUND_HEIGHT;


public class SnakeLegend {
    public Graphics2D graphics;
    private final int fontUnit = BACKGROUND_HEIGHT / 60;
    private final SavedGameDTO savedGameDTO;

    public SnakeLegend(Graphics2D graphics, SavedGameDTO savedGameDTO) {
        this.graphics = graphics;
        this.savedGameDTO = savedGameDTO;
    }

    public void drawLegend(int step) {
        switchAntiAliasing(graphics, true);
        drawInfo(step);
        switchAntiAliasing(graphics, false);
    }

    /**
     * Displays information about snakes score and number of steps game went.
     */
    public void drawInfo(int step) {
        graphics.setColor(Colors.TEXT.getColor());
        graphics.setFont(new Font("Arial", Font.BOLD, (int) (fontUnit * 1.5)));
        graphics.drawString("Steps: " + step, Settings.GRID_COLUMNS + fontUnit * 2, fontUnit * 4);
        var scoresThisStep = savedGameDTO.scores.get(step);
        for (int i = 0; i < scoresThisStep.length; i++) {
            drawScoreInfo(step, i);
        }
    }

    private void drawScoreInfo(int step, int i) {
        drawFilledRect(Settings.GRID_COLUMNS + fontUnit * 2, fontUnit * 4 * i + fontUnit * 7, i);
        graphics.setColor(Colors.TEXT.getColor());
        graphics.setFont(new Font("Arial", Font.BOLD, (int) (fontUnit * 1.5)));
        graphics.drawString(String.valueOf(savedGameDTO.scores.get(step)[i]), Settings.GRID_COLUMNS + fontUnit * 6, fontUnit * 4 * i + (int) (fontUnit * 8.7));
    }

    private void drawFilledRect(int x, int y, int i) {
        int squareSize = Settings.BACKGROUND_HEIGHT / 25;
        int fillSquareSize = squareSize - 1;
        graphics.drawRect(x, y, squareSize, squareSize);
        graphics.setColor(Colors.getColor(i + 3, 200)); // +3 because 1 is wall and 2 is food
        graphics.fillRect(x + 1, y + 1, fillSquareSize, fillSquareSize);
    }

    /**
     * Switches anti-aliasing on or off.
     *
     * @param isOn whether AA should be turned on
     */
    public static void switchAntiAliasing(Graphics2D graphics, boolean isOn) {
        if (isOn) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
    }
}
