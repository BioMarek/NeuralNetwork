package visualizations.snakeGraphic.explanations;

public class GraphBar implements Comparable<GraphBar> {
    public GraphBar(int xPosition, int yPosition, int height) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.height = height;
    }

    public int xPosition;
    public int yPosition;
    public int height;

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
