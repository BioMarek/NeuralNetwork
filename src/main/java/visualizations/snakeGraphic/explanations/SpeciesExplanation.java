package visualizations.snakeGraphic.explanations;

import utils.Colors;
import utils.Settings;
import utils.Util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class SpeciesExplanation {
    public Graphics2D graphics;
    private final int startX;
    private final int startY;
    private final int startDrawing;
    public int slowFrame = 0;
    private float networkScale = 1.0f;
    private int[][] weights = new int[8][4];

    public SpeciesExplanation(int startX, int startY, int startDrawing) {
        this.startX = startX;
        this.startY = startY;
        this.startDrawing = startDrawing;

        for (int l = 0; l < 8; l++) {
            for (int r = 0; r < 4; r++) {
                weights[l][r] = Util.randomInt(0, 255);
            }
        }
    }

    public void drawSpeciesExplanation() {
        drawNetwork(startX, startY, startDrawing);
    }

    public void drawNetwork(int startX, int startY, int startAppearingFrame) {
        int nodeSize = (int) (50 * networkScale);
        int nodeGap = (int) (80 * networkScale);
        drawLayer(startX, startY, 8, nodeSize, nodeGap, startAppearingFrame);
        drawLayer(startX + nodeSize * 6, startY + nodeGap * 2 + nodeSize / 2, 4, nodeSize, nodeGap, startAppearingFrame);
        drawWeights(startX, startY, nodeSize, nodeGap, 8, 4);
    }

    public void drawLayer(int startX, int startY, int nodes, int nodeSize, int nodeGap, int startAppearingFrame) {
        var alpha = calculateAppearingAlpha(startAppearingFrame);
        graphics.setColor(Colors.textWithAlpha(alpha));
        graphics.setStroke(new BasicStroke(3f));
        for (int i = 0; i < nodes; i++) {
            graphics.drawOval(startX, startY + i * nodeGap, nodeSize, nodeSize);
        }
    }

    public void drawWeights(int startX, int startY, int nodeSize, int nodeGap, int leftNodes, int rightNodes) {
        List<Integer> leftYs = new ArrayList<>();
        List<Integer> rightYs = new ArrayList<>();
        int leftX = startX + nodeSize;
        int rightX = startX + nodeSize * 6;
        int rightYShift = startY + nodeGap * 2 + nodeSize / 2;

        for (int l = 0; l < leftNodes; l++) {
            leftYs.add(startY + nodeSize / 2 + l * nodeGap);
        }
        for (int r = 0; r < rightNodes; r++) {
            rightYs.add(rightYShift + nodeSize / 2 + r * nodeGap);
        }


        for (int l = 0; l < leftNodes; l++) {
            for (int r = 0; r < rightNodes; r++) {
                graphics.setColor(weightToColor(weights[l][r]));
                graphics.drawLine(leftX, leftYs.get(l), rightX, rightYs.get(r));
            }
        }
    }

    public int calculateAppearingAlpha(int startAppearingFrame) {
        if (startAppearingFrame + Settings.CHANGING_SLOW_FRAMES < slowFrame)
            return 255;
        else
            return (int) ((slowFrame - startAppearingFrame) / 15.0 * 255);
    }

    public Color weightToColor(int weight) {
        return new Color(weight, 255 - weight, 0, 200);
    }
}
