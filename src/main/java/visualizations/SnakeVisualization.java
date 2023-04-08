package visualizations;

import interfaces.GridVisualization;
import utils.Colors;
import utils.Settings;

import java.awt.Graphics2D;

public class SnakeVisualization implements GridVisualization {
    private Graphics2D graphics;

    @Override
    public void createNextFrame() {

    }

    @Override
    public void drawPresentation(Graphics2D graphics) {
        this.graphics = graphics;
        setBackground();
    }

    public void setBackground() {
        graphics.setColor(Colors.BACKGROUND.getColor());
        graphics.fillRect(0, 0, Settings.backgroundWidth, Settings.backgroundHeight);
    }
}
