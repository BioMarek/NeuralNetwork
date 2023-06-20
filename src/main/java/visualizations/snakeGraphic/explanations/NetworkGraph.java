package visualizations.snakeGraphic.explanations;

import utils.Colors;
import utils.Util;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class NetworkGraph {
    public Graphics2D graphics;
    public int slowFrame;
    public int fastFrame;
    private List<GraphBar> graphBars;
    private int BIG_FONT_SIZE = 27;
    private int SMALL_FONT_SIZE = 20;
    private int NUM_OF_BARS = 100;
    private int startX;
    private int startY;
    private int startDrawing;
    private int barGap = 15;

    public NetworkGraph() {
        graphBars = new ArrayList<>();
    }

    public void initConstants(int startX, int startY, int startDrawing) {
        this.startX = startX;
        this.startY = startY;
        this.startDrawing = startDrawing;
    }

    public void initGraphBars() {
        int min = 5;
        int max = 100;
        int currentX = startX;
        graphBars.add(new GraphBar(currentX, startY - 30, 30));
        for (int i = 1; i < NUM_OF_BARS; i++) {
            currentX += barGap;
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
                limit = Math.min(NUM_OF_BARS, slowFrame - startDrawing - sequenceDelay);

            for (int i = 0; i < limit; i++) {
                var graphBar = graphBars.get(i);
                graphics.fillRect(graphBar.xPosition, graphBar.yPosition, width, graphBar.height);
            }
        }
    }

    public void drawGraphAxis() {
        if (slowFrame > startDrawing) {
            graphics.setFont(new Font("Arial", Font.BOLD, BIG_FONT_SIZE));
            graphics.drawString("Score", startX - 75, startY - 190);
            graphics.setStroke(new BasicStroke(3f));
            graphics.setColor(Colors.TEXT.getColor());

            // axis
            graphics.drawLine(startX, startY, startX + barGap * NUM_OF_BARS + 10, startY); // x axis
            graphics.drawLine(startX, startY, startX, startY - 170); // y axis

            // horizontal ticks
            graphics.setFont(new Font("Arial", Font.PLAIN, SMALL_FONT_SIZE));
            for (int i = 1; i < 4; i++) {
                graphics.drawLine(startX - 10, startY - 50 * i, startX, startY - 50 * i);
                var number = String.valueOf(50 * i);
                var leftShift = number.length() == 3 ? 50 : 39; // only for length 2 an 3
                graphics.drawString(number, startX - leftShift, startY + 8 - 50 * i);
            }
        }
    }
}
