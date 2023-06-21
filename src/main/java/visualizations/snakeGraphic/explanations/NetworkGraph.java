package visualizations.snakeGraphic.explanations;

import utils.Colors;
import utils.Pair;
import utils.Util;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
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
    private int numOfBarMoves = 60;
    private int cutoffDelay = 238; // 300 - 62
    private int eliminatedDisappearDelay = 268; // 330 - 62
    private int graphBarMoveIndex = 0;

    public NetworkGraph(int startX, int startY, int startDrawing) {
        graphBars = new ArrayList<>();
        this.startX = startX;
        this.startY = startY;
        this.startDrawing = startDrawing;

        initGraphBars();
        calculateNewGraphBarPosition(true);
    }

    public void initGraphBars() {
        int min = 5;
        int max = 100;
        int currentX = startX;
        var firstGraphBar = new GraphBar(currentX, startY - 30, 30);
        firstGraphBar.isVisible = true;
        graphBars.add(firstGraphBar);
        for (int i = 1; i < NUM_OF_BARS; i++) {
            currentX += barGap;
            int height = Util.randomInt(min, max);
            graphBars.add(new GraphBar(currentX, startY - height, height));
        }
    }

    public void drawGraphBars(int startMovingBars) {
        int width = 10;
        int sequenceDelay = 30;
        int limit = 1;
        if (slowFrame > startDrawing) {
            if (slowFrame > startDrawing + sequenceDelay) {
                limit = Math.min(NUM_OF_BARS - 1, slowFrame - startDrawing - sequenceDelay);
                graphBars.get(limit).isVisible = true;
            }
            if (slowFrame > startMovingBars) {
                graphBarMoveIndex = Math.min(numOfBarMoves - 1, slowFrame - startMovingBars);
                for (int i = 0; i < NUM_OF_BARS; i++) {
                    var graphBar = graphBars.get(i);
                    graphBar.currentCoordinates = graphBar.moveCoordinatesList.get(graphBarMoveIndex);
                }
            }

            if (slowFrame == startDrawing + eliminatedDisappearDelay - 1)
                calculateNewGraphBarPosition(false);
            if (slowFrame > startDrawing + eliminatedDisappearDelay)
                for (int i = 90; i < NUM_OF_BARS; i++)
                    graphBars.get(i).reduceAlpha();

            for (int i = 0; i < NUM_OF_BARS; i++) {
                var graphBar = graphBars.get(i);
                graphics.setColor(graphBar.currentColor);
                if (graphBar.isVisible)
                    graphics.fillRect(graphBar.currentCoordinates.getFirst() + 2, graphBar.currentCoordinates.getSecond() - 1, width, graphBar.height); // -2 and -1 so the bars are not over axis
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

            // last 10 cutoff
            if (slowFrame > startDrawing + cutoffDelay) {
                graphics.setColor(Colors.redWithAlpha(200));
                graphics.drawLine(startX + 90 * barGap, startY - 2, startX + 90 * barGap, startY - 170); // cutOffLine
            }
        }
    }

    public void calculateNewGraphBarPosition(boolean withCopy) {
        for (int i = 0; i < graphBars.size(); i++) {
            graphBars.get(i).oldPosition = i;
        }

        var graphBarsCopy = new ArrayList<>(graphBars);
        if (withCopy) {
            graphBarsCopy.sort(Collections.reverseOrder());
        } else {
            graphBars.sort(Collections.reverseOrder());
        }

        for (int i = 0; i < graphBarsCopy.size(); i++) {
            var graphBar = graphBarsCopy.get(i);
            var endCoordinate = new Pair<>(startX + barGap * i, startY - graphBar.height);
            graphBar.newPosition = i;
            graphBar.calculateMoveCoordinates(numOfBarMoves, endCoordinate);
        }
    }
}
