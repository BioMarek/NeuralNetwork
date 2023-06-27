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
    private final List<GraphBar> graphBars;
    private final List<GraphBar> moveGraphBars;
    private final int BIG_FONT_SIZE = 27;
    private final int SMALL_FONT_SIZE = 20;
    private final int NUM_OF_BARS = 100;
    private final int NUM_MOVE_BARS = 30;
    private final int startX;
    private final int startY;
    private final int startDrawing;
    private final int barGap = 15;
    private final int numOfBarMoves = 60;
    private int currentGeneration = 1;
    private int showGenerationTextDelay = 148; // x - 62
    private int cutoffDelay = 268;
    private int eliminatedDisappearDelay = 298;
    private int lateralMoveDelay = 328;
    private int graphBarMoveIndex = 0;
    private int showFirstMutationDelay = 418;
    private int hideBarsForNextGeneration = 478;
    private int showSecondGeneration = 508;
    private int showThirdGeneration = 538;
    private int showFourthGeneration = 568;
    private int showFifthGeneration = 598;
    private int showSixthGeneration = 629;
    private double averageHeight;

    public NetworkGraph(int startX, int startY, int startDrawing) {
        graphBars = new ArrayList<>();
        moveGraphBars = new ArrayList<>();
        this.startX = startX;
        this.startY = startY;
        this.startDrawing = startDrawing;

        initGraphBars();
        averageHeight = graphBars.stream().mapToDouble(bar -> bar.height)
                .average()
                .orElse(0);
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
        int limit;
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
            if (slowFrame > (startDrawing + eliminatedDisappearDelay) && slowFrame < (startDrawing + showSecondGeneration))
                for (int i = (NUM_OF_BARS - NUM_MOVE_BARS); i < NUM_OF_BARS; i++)
                    graphBars.get(i).reduceAlpha();

            if (slowFrame == (startDrawing + lateralMoveDelay))
                calculateNewGraphBarPositionLateralMove();
            if (slowFrame > (startDrawing + lateralMoveDelay) && slowFrame < (startDrawing + showSecondGeneration)) {
                for (int i = 0; i < NUM_MOVE_BARS; i++) {
                    var graphBar = moveGraphBars.get(i);
                    var lateralMoveIndex = Math.min(numOfBarMoves - 1, slowFrame - (startDrawing + lateralMoveDelay));
                    graphBar.currentCoordinates = graphBar.moveCoordinatesList.get(lateralMoveIndex);
                    graphics.setColor(graphBar.currentColor);
                    graphics.fillRect(graphBar.currentCoordinates.getFirst() + 2, graphBar.currentCoordinates.getSecond() - 1, width, graphBar.height); // +2 and -1 so the bars are not over axis
                }
            }

            if (slowFrame > (startDrawing + showFirstMutationDelay) && slowFrame < (startDrawing + showSecondGeneration)) {
                for (GraphBar graphBar : moveGraphBars) {
                    graphBar.currentColor = calculateChangingColor(Colors.lightGreenWithAlpha(255), Colors.lightLightVioletWithAlpha(255), startDrawing + showFirstMutationDelay);
                }
            }

            if (slowFrame > (startDrawing + hideBarsForNextGeneration) && slowFrame < (startDrawing + showSecondGeneration)) {
                for (GraphBar graphBar : graphBars) {
                    graphBar.reduceAlpha();
                }
                for (GraphBar graphBar : moveGraphBars)
                    graphBar.reduceAlpha();
            }

            if (slowFrame == (startDrawing + showSecondGeneration)) {
                prepareNextGeneration(6, 110);
                currentGeneration = 2;
            }

            if (slowFrame > (startDrawing + showSecondGeneration) && slowFrame < (startDrawing + showThirdGeneration)) {
                for (GraphBar graphBar : graphBars) {
                    graphBar.height = Math.min(graphBar.height + 5, graphBar.maxHeight);
                    graphBar.currentCoordinates.setSecond(startY - graphBar.height);
                }
            }

            if (slowFrame == (startDrawing + showThirdGeneration)) {
                prepareNextGeneration(7, 120);
                currentGeneration = 3;
            }

            if (slowFrame > (startDrawing + showThirdGeneration) && slowFrame < (startDrawing + showFourthGeneration)) {
                for (GraphBar graphBar : graphBars) {
                    graphBar.height = Math.min(graphBar.height + 5, graphBar.maxHeight);
                    graphBar.currentCoordinates.setSecond(startY - graphBar.height);
                }
            }

            if (slowFrame == (startDrawing + showFourthGeneration)) {
                prepareNextGeneration(8, 130);
                currentGeneration = 4;
            }

            if (slowFrame > (startDrawing + showFourthGeneration) && slowFrame < (startDrawing + showFifthGeneration)) {
                for (GraphBar graphBar : graphBars) {
                    graphBar.height = Math.min(graphBar.height + 5, graphBar.maxHeight);
                    graphBar.currentCoordinates.setSecond(startY - graphBar.height);
                }
            }

            if (slowFrame == (startDrawing + showFifthGeneration)) {
                prepareNextGeneration(9, 140);
                currentGeneration = 5;
            }

            if (slowFrame > (startDrawing + showFifthGeneration) && slowFrame < (startDrawing + showSixthGeneration)) {
                for (GraphBar graphBar : graphBars) {
                    graphBar.height = Math.min(graphBar.height + 5, graphBar.maxHeight);
                    graphBar.currentCoordinates.setSecond(startY - graphBar.height);
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
            drawText("Score", startX - 75, startY - 190);
            graphics.setStroke(new BasicStroke(3f));

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

            // generations and average
            if (slowFrame > (startDrawing + showGenerationTextDelay)) {
                drawText("Generation  " + currentGeneration, startX - 165 + 46 * barGap, startY - 440);
                drawText("avg. score:  " + averageHeight, startX - 165 + 46 * barGap, startY - 400);
            }


            // last 30 cutoff
            if (slowFrame > startDrawing + cutoffDelay) {
                graphics.setColor(Colors.redWithAlpha(200));
                graphics.drawLine(startX + (NUM_OF_BARS - NUM_MOVE_BARS) * barGap, startY - 2, startX + (NUM_OF_BARS - NUM_MOVE_BARS) * barGap, startY - 170); // cutOffLine
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
            graphBar.calculateJumpCoordinates(numOfBarMoves, -200, graphBar.currentCoordinates, new Pair<>(currentX + (NUM_OF_BARS - NUM_MOVE_BARS) * barGap, startY - height));
            moveGraphBars.add(graphBar);
            currentX += barGap;
        }
    }

    public void prepareNextGeneration(int minHeight, int maxHeight) {
        for (GraphBar graphBar : graphBars) {
            graphBar.currentColor = Colors.lightGreenWithAlpha(255);
            graphBar.height = Util.randomInt(minHeight, maxHeight);
            graphBar.currentCoordinates.setSecond(startY);
        }

        averageHeight = graphBars.stream().mapToDouble(bar -> bar.height)
                .average()
                .orElse(0);
        graphBars.sort(Collections.reverseOrder());

        for (int i = 0; i < graphBars.size(); i++) {
            var graphBar = graphBars.get(i);
            graphBar.currentCoordinates = new Pair<>(startX + barGap * i, startY - graphBar.height);
            graphBar.maxHeight = graphBar.height;
            graphBar.height = 0;
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

    public void drawText(String text, float startX, float startY) {
        graphics.setColor(Colors.TEXT.getColor());
        graphics.setFont(new Font("Arial", Font.BOLD, BIG_FONT_SIZE));
        graphics.drawString(text, startX, startY);
    }
}
