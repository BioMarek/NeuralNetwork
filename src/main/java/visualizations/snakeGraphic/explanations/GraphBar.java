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

    public GraphBar(int xPosition, int yPosition, int height) {
        this.currentCoordinates = new Pair<>(xPosition, yPosition);
        this.height = height;
        this.moveCoordinatesList = new ArrayList<>();
        this.moveCoordinatesList.add(currentCoordinates);
        this.currentColor = Colors.lightGreenWithAlpha(255);
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
