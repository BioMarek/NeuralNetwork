package visualizations.snakeGraphic.explanations;

import utils.Colors;
import utils.Pair;
import utils.Settings;
import utils.Util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkGraph {
    public Graphics2D graphics;
    public int slowFrame;
    private List<GraphBar> graphBars;
    private List<GraphBar> moveGraphBars;
    private int BIG_FONT_SIZE = 27;
    private int SMALL_FONT_SIZE = 20;
    private int NUM_OF_BARS = 100;
    private int NUM_MOVE_BARS = 10;
    private int startX;
    private int startY;
    private int startDrawing;
    private int barGap = 15;
    private int numOfBarMoves = 60;
    private int cutoffDelay = 238; // 300 - 62
    private int eliminatedDisappearDelay = 268; // 330 - 62
    private int lateralMoveDelay = 298; // 360 - 62
    private int graphBarMoveIndex = 0;
    private int showFirstMutationDelay = 388; // 390 - 62
    private int showSecondMutationDelay = 448; // 420 - 62

    public NetworkGraph(int startX, int startY, int startDrawing) {
        graphBars = new ArrayList<>();
        moveGraphBars = new ArrayList<>();
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
            if (slowFrame > startMovingBars && slowFrame <= startMovingBars + numOfBarMoves) {
                graphBarMoveIndex = Math.min(numOfBarMoves - 1, slowFrame - startMovingBars);
                for (int i = 0; i < NUM_OF_BARS; i++) {
                    var graphBar = graphBars.get(i);
                    graphBar.currentCoordinates = graphBar.moveCoordinatesList.get(graphBarMoveIndex);
                }
            }
            if (slowFrame == startDrawing + eliminatedDisappearDelay)
                calculateNewGraphBarPosition(false);
            if (slowFrame > startDrawing + eliminatedDisappearDelay)
                for (int i = 90; i < NUM_OF_BARS; i++)
                    graphBars.get(i).reduceAlpha();

            if (slowFrame == (startDrawing + lateralMoveDelay))
                calculateNewGraphBarPositionLateralMove();
            if (slowFrame > (startDrawing + lateralMoveDelay)) {
                for (int i = 0; i < NUM_MOVE_BARS; i++) {
                    var graphBar = moveGraphBars.get(i);
                    var lateralMoveIndex = Math.min(numOfBarMoves - 1, slowFrame - (startDrawing + lateralMoveDelay));
                    graphBar.currentCoordinates = graphBar.moveCoordinatesList.get(lateralMoveIndex);
                    graphics.setColor(graphBar.currentColor);
                    graphics.fillRect(graphBar.currentCoordinates.getFirst() + 2, graphBar.currentCoordinates.getSecond() - 1, width, graphBar.height); // +2 and -1 so the bars are not over axis
                }
            }

            if (slowFrame > (startDrawing + showFirstMutationDelay)) {
                for (GraphBar graphBar : moveGraphBars) {
                    graphBar.currentColor = calculateChangingColor(Colors.lightGreenWithAlpha(255), Colors.lightLightVioletWithAlpha(255), startDrawing + showFirstMutationDelay);
                }
            }

            if (slowFrame > (startDrawing + showSecondMutationDelay)) {
                for (GraphBar graphBar : graphBars) {
                    if (graphBar.newPosition >= 80 && graphBar.newPosition < 90)
                        graphBar.currentColor = calculateChangingColor(Colors.lightGreenWithAlpha(255), Colors.lightLightVioletWithAlpha(255), startDrawing + showSecondMutationDelay);
                }
            }

            for (int i = 0; i < NUM_OF_BARS; i++) {
                var graphBar = graphBars.get(i);
                if (graphBar.isVisible) {
                    graphics.setColor(graphBar.currentColor);
                    graphics.fillRect(graphBar.currentCoordinates.getFirst() + 2, graphBar.currentCoordinates.getSecond() - 1, width, graphBar.height); // +2 and -1 so the bars are not over axis
                }
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

    public void calculateNewGraphBarPositionLateralMove() {
        int currentX = startX;
        for (int i = 0; i < NUM_MOVE_BARS; i++) {
            var height = graphBars.get(i).height;
            var graphBar = new GraphBar(currentX, startY - height, height);
            graphBar.calculateJumpCoordinates(numOfBarMoves, -200, graphBar.currentCoordinates, new Pair<>(currentX + 90 * barGap, startY - height));
            moveGraphBars.add(graphBar);
            currentX += barGap;
        }
    }

    public Color calculateChangingColor(Color from, Color to, int startChangingFrame) {
        int stage;
        if (slowFrame < startChangingFrame)
            stage = 0;
        else if (slowFrame >= startChangingFrame + Settings.CHANGING_SLOW_FRAMES) {
            stage = Settings.CHANGING_SLOW_FRAMES;
        } else
            stage = slowFrame - startChangingFrame;
        return Colors.colorMixing(from, to, stage);
    }
}
