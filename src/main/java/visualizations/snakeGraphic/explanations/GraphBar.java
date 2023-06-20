package visualizations.snakeGraphic.explanations;

import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class GraphBar implements Comparable<GraphBar> {
    public List<Pair<Integer>> moveCoordinatesList;
    public Pair<Integer> currentCoordinates;
    public int height;
    public int oldPosition;
    public int newPosition;

    public GraphBar(int xPosition, int yPosition, int height) {
        this.currentCoordinates = new Pair<>(xPosition, yPosition);
        this.height = height;
        this.moveCoordinatesList = new ArrayList<>();
        this.moveCoordinatesList.add(currentCoordinates);
    }

    public void calculateMoveCoordinates(int steps, Pair<Integer> endCoordinate) {
        // TODO fix non smooth movement of bars
        moveCoordinatesList.clear();
        for (int i = 0; i < steps; i++) {
            int x = (int) (currentCoordinates.getFirst() + (endCoordinate.getFirst() - currentCoordinates.getFirst()) * i / steps * 1.0);
            int y = (int) (currentCoordinates.getSecond() + (endCoordinate.getSecond() - currentCoordinates.getSecond()) * i / steps * 1.0);
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
