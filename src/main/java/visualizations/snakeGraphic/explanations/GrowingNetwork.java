package visualizations.snakeGraphic.explanations;

import utils.Colors;
import utils.Settings;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class GrowingNetwork {
    public Graphics2D graphics;
    private int startX;
    private int startY;
    public int slowFrame = 0;
    private float networkScale = 0.0f;
    private int endX;
    private int endY;
    private int xShift;
    private int yShift;

    public GrowingNetwork(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.xShift = (endX - startX) / 100;
        this.yShift = (endY - startY) / 100;
    }

    public void drawGrowingNetwork(int startAppearingFrame, int startGrowing) {
        if (slowFrame > startAppearingFrame) {
            if (slowFrame > startGrowing) {
                networkScale = Math.min(networkScale + 0.02f, 0.3f);
                startX = Math.min(startX + xShift, endX);
                startY = Math.max(startY + yShift, endY);
            }
            if (networkScale > 0.02)
                drawNetworkWithHiddenLayer(startX, startY, 1, startAppearingFrame);
        }
    }

    public void drawNetworkWithHiddenLayer(int startX, int startY, int hiddenNodes, int startAppearingFrame) {
        int nodeSize = (int) (50 * networkScale);
        int nodeGap = (int) (80 * networkScale);
        int INPUTS = 8;
        int hiddenLayerShift = nodeGap * (INPUTS - hiddenNodes) / 2;
        drawLayer(startX, startY, INPUTS, nodeSize, nodeGap, startAppearingFrame);
        drawLayer(startX + nodeSize * 4, startY + hiddenLayerShift, 1, nodeSize, nodeGap, startAppearingFrame);
        drawLayer(startX + nodeSize * 8, startY + nodeGap * 2, 4, nodeSize, nodeGap, startAppearingFrame);
    }

    public void drawLayer(int startX, int startY, int nodes, int nodeSize, int nodeGap, int startAppearingFrame) {
        var alpha = calculateAppearingAlpha(startAppearingFrame);
        graphics.setColor(Colors.textWithAlpha(alpha));
        graphics.setStroke(new BasicStroke(3f));
        for (int i = 0; i < nodes; i++) {
            graphics.drawOval(startX, startY + i * nodeGap, nodeSize, nodeSize);
        }
    }

    public int calculateAppearingAlpha(int startAppearingFrame) {
        if (startAppearingFrame + Settings.CHANGING_SLOW_FRAMES < slowFrame)
            return 255;
        else
            return (int) ((slowFrame - startAppearingFrame) / 15.0 * 255);
    }
}
