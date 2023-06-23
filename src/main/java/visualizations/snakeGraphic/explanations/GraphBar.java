package visualizations.snakeGraphic.explanations;

import utils.Colors;
import utils.Pair;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GraphBar implements Comparable<GraphBar> {
    public List<Pair<Integer>> moveCoordinatesList;
    public Pair<Integer> currentCoordinates;
    public int height;
    public int oldPosition;
    public int newPosition;
    public boolean isVisible = false;
    public Color currentColor;
    public int alphaReductionStep = 255 / 30;
    public int currentAlpha = 255;

    public GraphBar(int xPosition, int yPosition, int height) {
        this.currentCoordinates = new Pair<>(xPosition, yPosition);
        this.height = height;
        this.moveCoordinatesList = new ArrayList<>();
        this.moveCoordinatesList.add(currentCoordinates);
        this.currentColor = Colors.lightGreenWithAlpha(currentAlpha);
    }

    public void calculateMoveCoordinates(int steps, Pair<Integer> endCoordinate) {
        moveCoordinatesList.clear();
        for (int i = 0; i < steps; i++) {
            int x = (int) (currentCoordinates.getFirst() + (endCoordinate.getFirst() - currentCoordinates.getFirst()) * i / (steps - 1) * 1.0);
            int y = (int) (currentCoordinates.getSecond() + (endCoordinate.getSecond() - currentCoordinates.getSecond()) * i / (steps - 1) * 1.0);
            if (i == steps - 1)
                moveCoordinatesList.add(endCoordinate);
            else
                moveCoordinatesList.add(new Pair<>(x, y));
        }
    }

    public void calculateJumpCoordinates(int steps, int jumpHeight, Pair<Integer> startCoordinate, Pair<Integer> endCoordinate) {
        moveCoordinatesList.clear();
        moveCoordinatesList.add(startCoordinate);
        int jumpSteps = (int) (steps * 0.15);
        int lastX = 0;
        int lastY = 0;

        // move up
        for (int i = 1; i < jumpSteps; i++) {
            int x = startCoordinate.getFirst();
            int y = (int) (startCoordinate.getSecond() + jumpHeight * i / (jumpSteps - 1) * 1.0);
            moveCoordinatesList.add(new Pair<>(x, y));
            lastY = y;
        }

        // move to the side
        for (int i = 0; i < steps - 2 * jumpSteps; i++) {
            int x = (int) (startCoordinate.getFirst() + (endCoordinate.getFirst() - startCoordinate.getFirst()) * (i + 1) / (steps - (2 * jumpSteps)) * 1.0);
            int y = startCoordinate.getSecond() + jumpHeight;
            moveCoordinatesList.add(new Pair<>(x, y));
            lastX = x;
        }

        // move down
        for (int i = 1; i < jumpSteps; i++) {
            int y = (int) (lastY - jumpHeight * i / (jumpSteps - 1) * 1.0);
            moveCoordinatesList.add(new Pair<>(lastX, y));
        }

        moveCoordinatesList.add(endCoordinate);
        moveCoordinatesList.add(endCoordinate);
    }

    public void reduceAlpha() {
        currentAlpha = Math.max(0, currentAlpha - alphaReductionStep);
        if (currentColor.getRed() == 143)
            currentColor = Colors.lightGreenWithAlpha(currentAlpha);
        else
            currentColor = Colors.lightLightVioletWithAlpha(currentAlpha);
    }

    @Override
    public int compareTo(GraphBar graphBar) {
        if (graphBar.height < height)
            return 1;
        else if (graphBar.height > height) {
            return -1;
        }
        return 0;
    }
}
