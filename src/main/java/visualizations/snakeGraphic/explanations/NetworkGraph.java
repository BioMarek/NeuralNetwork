package visualizations.snakeGraphic.explanations;

import utils.Colors;
import utils.Util;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class NetworkGraph {
    public Graphics2D graphics;
    public int slowFrame;
    public int fastFrame;
    private List<GraphBar> graphBars;
    private int NUM_OF_BARS = 60;
    private int startX;
    private int startY;
    private int startDrawing;

    public NetworkGraph() {
        graphBars = new ArrayList<>();
    }

    public void initConstants(int startX, int startY, int startDrawing){
        this.startX = startX;
        this.startY = startY;
        this.startDrawing = startDrawing;
    }

    public void initGraphBars() {
        int gap = 20;
        int min = 5;
        int max = 100;
        int currentX = startX;
        graphBars.add(new GraphBar(currentX, startY - 50, 50));
        for (int i = 1; i < NUM_OF_BARS; i++) {
            currentX += gap;
            int height = Util.randomInt(min, max);
            graphBars.add(new GraphBar(currentX, startY - height, height));
        }
    }

    public void drawNetworkGraph() {
        int width = 10;
        int sequenceDelay = 30;
        int limit = 1;
        if (slowFrame > startDrawing) {
            graphics.setColor(Colors.TEXT.getColor());
            if (slowFrame > startDrawing + sequenceDelay)
                limit = Math.min(50, slowFrame - startDrawing - sequenceDelay);

            for (int i = 0; i < limit; i++) {
                var graphBar = graphBars.get(i);
                graphics.fillRect(graphBar.xPosition, graphBar.yPosition, width, graphBar.height);
            }
        }
    }

    public void drawGraphAxis() {
        if (slowFrame > startDrawing){
            graphics.setColor(Colors.TEXT.getColor());
            graphics.drawLine(startX, startY, startX + 20 * NUM_OF_BARS, startY);
            graphics.drawLine(startX, startY, startX, startY - 150);
        }
    }

    public static class GraphBar {
        public GraphBar(int xPosition, int yPosition, int height) {
            this.xPosition = xPosition;
            this.yPosition = yPosition;
            this.height = height;
        }

        public int xPosition;
        public int yPosition;
        public int height;
    }
}
