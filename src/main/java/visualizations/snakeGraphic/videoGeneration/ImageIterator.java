package visualizations.snakeGraphic.videoGeneration;

import games.snake.savegame.SavedGameDTO;
import utils.Settings;
import visualizations.snakeGraphic.GridVisualization;

import java.awt.image.BufferedImage;
import java.util.Iterator;

public class ImageIterator implements Iterator<BufferedImage> {
    private final GridVisualization gridVisualization;
    private BufferedImage bImg;
    private int currentFrame = 0;

    public ImageIterator(GridVisualization gridVisualization) {
        this.gridVisualization = gridVisualization;
    }

    @Override
    public boolean hasNext() {
        return !gridVisualization.stopped();
    }

    @Override
    public BufferedImage next() {
        gridVisualization.createNextFrame();
        return createBufferedImage();
    }

    public BufferedImage last() {
        return bImg;
    }

    private BufferedImage createBufferedImage() {
        bImg = new BufferedImage(Settings.BACKGROUND_WIDTH, Settings.BACKGROUND_HEIGHT, BufferedImage.TYPE_INT_RGB);
        gridVisualization.drawPresentation(bImg.createGraphics());
        return bImg;
    }
}
