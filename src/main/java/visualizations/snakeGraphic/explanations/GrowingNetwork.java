package visualizations.snakeGraphic.explanations;

import utils.Colors;
import utils.Settings;
import utils.Util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

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
    private double chanceToDrawWeight = 0.7d;
    private int[][] weights = new int[8][4];
    private int hiddenLayerNodes;

    public GrowingNetwork(int startX, int startY, int endX, int endY, int hiddenLayerNodes) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.hiddenLayerNodes = hiddenLayerNodes;
        this.xShift = (endX - startX) / 100;
        this.yShift = (endY - startY) / 100;

        for (int l = 0; l < 8; l++) {
            for (int r = 0; r < 4; r++) {
                if (Util.isRandomChanceTrue(chanceToDrawWeight))
                    weights[l][r] = Util.randomInt(0, 255);
                else
                    weights[l][r] = 0;
            }
        }
    }

    public void drawGrowingNetwork(int startAppearingFrame, int startGrowing) {
        if (slowFrame > startAppearingFrame) {
            if (slowFrame > startGrowing) {
                networkScale = Math.min(networkScale + 0.02f, 0.3f);
                startX = startX != endX ? startX + xShift : endX;
                startY = startY != endY ? startY + yShift : endY;
            }
            if (networkScale > 0.02)
                drawNetworkWithHiddenLayer(startX, startY, startAppearingFrame);
        }
    }

    public void drawNetworkWithHiddenLayer(int startX, int startY, int startAppearingFrame) {
        int nodeSize = (int) (50 * networkScale);
        int nodeGap = (int) (80 * networkScale);
        int INPUTS = 8;
        int hiddenLayerShift = nodeGap * (INPUTS - hiddenLayerNodes) / 2;
        drawWeights(startX, startY, nodeSize, nodeGap, 8, 4, startAppearingFrame);
        drawLayer(startX, startY, INPUTS, nodeSize, nodeGap, startAppearingFrame);
        drawLayer(startX + nodeSize * 4, startY + hiddenLayerShift, hiddenLayerNodes, nodeSize, nodeGap, startAppearingFrame);
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

    public void drawWeights(int startX, int startY, int nodeSize, int nodeGap, int leftNodes, int rightNodes, int startAppearingFrame) {
        var alpha = calculateAppearingAlpha(startAppearingFrame);
        List<Integer> leftYs = new ArrayList<>();
        List<Integer> rightYs = new ArrayList<>();
        int leftX = startX + nodeSize;
        int rightX = startX + nodeSize * 8;
        int rightYShift = startY + nodeGap * 2;

        for (int l = 0; l < leftNodes; l++) {
            leftYs.add(startY + nodeSize / 2 + l * nodeGap);
        }
        for (int r = 0; r < rightNodes; r++) {
            rightYs.add(rightYShift + nodeSize / 2 + r * nodeGap);
        }

        graphics.setStroke(new BasicStroke(3f));
        for (int l = 0; l < leftNodes; l++) {
            for (int r = 0; r < rightNodes; r++) {
                graphics.setColor(weightToColor(weights[l][r], alpha));
                graphics.drawLine(leftX, leftYs.get(l), rightX, rightYs.get(r));
                graphics.setStroke(new BasicStroke(3f));

            }
        }
    }

    public Color weightToColor(int weight, int alpha) {
        if (weight == 0)
            alpha = 0;
        return new Color(weight, 255 - weight, 0, alpha);
    }
}
