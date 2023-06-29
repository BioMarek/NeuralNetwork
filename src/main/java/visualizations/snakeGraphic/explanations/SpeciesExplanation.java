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
import java.util.List;

public class SpeciesExplanation {
    private final int FONT_SIZE = (int) (Settings.BACKGROUND_HEIGHT / 60 * 1.2);
    public Graphics2D graphics;
    private final int startX;
    private final int startY;
    private final int startDrawing;
    public int slowFrame = 0;
    private float networkScale = 1.0f;
    private int[][] weights = new int[8][4];
    List<Pair<Integer>> changingWeights;
    private int startChangingWeights = 45;

    public SpeciesExplanation(int startX, int startY, int startDrawing) {
        this.startX = startX;
        this.startY = startY;
        this.startDrawing = startDrawing;

        for (int l = 0; l < 8; l++) {
            for (int r = 0; r < 4; r++) {
                weights[l][r] = Util.randomInt(0, 255);
            }
        }
        initChangingWeights();
    }

    public void initChangingWeights() {
        changingWeights = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            changingWeights.add(new Pair<>(Util.randomInt(0, 32), Util.randomInt(0, 255)));
        }
    }

    public void drawSpeciesExplanation() {
        drawNetwork(startX, startY, startDrawing);
    }

    public void drawNetwork(int startX, int startY, int startAppearingFrame) {
        int nodeSize = (int) (50 * networkScale);
        int nodeGap = (int) (80 * networkScale);
        drawWeights(startX, startY, nodeSize, nodeGap, 8, 4, startAppearingFrame);
        drawLayer(startX, startY, 8, nodeSize, nodeGap, startAppearingFrame);
        drawLayer(startX + nodeSize * 6, startY + nodeGap * 2, 4, nodeSize, nodeGap, startAppearingFrame);
        drawWeightColorLegend(startX + 100, startY + 700, startAppearingFrame);
    }

    public void drawLayer(int startX, int startY, int nodes, int nodeSize, int nodeGap, int startAppearingFrame) {
        var alpha = calculateAppearingAlpha(startAppearingFrame);
        graphics.setColor(Colors.textWithAlpha(alpha));
        graphics.setStroke(new BasicStroke(3f));
        for (int i = 0; i < nodes; i++) {
            graphics.drawOval(startX, startY + i * nodeGap, nodeSize, nodeSize);
        }
    }

    public void drawWeights(int startX, int startY, int nodeSize, int nodeGap, int leftNodes, int rightNodes, int startAppearingFrame) {
        var alpha = calculateAppearingAlpha(startAppearingFrame);
        List<Integer> leftYs = new ArrayList<>();
        List<Integer> rightYs = new ArrayList<>();
        int leftX = startX + nodeSize;
        int rightX = startX + nodeSize * 6;
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
                for (int i = 0; i < 5; i++) {
                    showWeightBlink(l, r, i);
                }
                graphics.setColor(weightToColor(weights[l][r], alpha));
                graphics.drawLine(leftX, leftYs.get(l), rightX, rightYs.get(r));
                graphics.setStroke(new BasicStroke(3f));
            }
        }
    }

    public void showWeightBlink(int l, int r, int index) {
        if (slowFrame > startChangingWeights + 20 * index && l * 4 + r == changingWeights.get(index).getFirst()) {
            weights[l][r] = changingWeights.get(0).getSecond();
            var thickness = calculateThickness(slowFrame - startChangingWeights - 20 * index);
            graphics.setStroke(new BasicStroke(thickness));
        }
    }

    public float calculateThickness(int stage) {
        return switch (stage) {
            case 1, 2, 3 -> 9f;
            case 4, 5, 6 -> 8f;
            case 7, 8, 9 -> 7f;
            case 10, 11, 12 -> 6f;
            case 13, 14, 15 -> 5f;
            case 16, 17, 18 -> 4f;
            default -> 3f;
        };
    }

    public void drawWeightColorLegend(int startX, int startY, int startAppearingFrame) {
        var alpha = calculateAppearingAlpha(startAppearingFrame);
        graphics.setColor(Colors.textWithAlpha(alpha));
        graphics.fillRect(startX - 1, startY - 1, 152, 32);
        var innerX = startX + 4;
        var innerY = startY + 4;

        for (int i = 0; i < 144; i++) {
            int mockWeightColor = (int) (255.0 / 144.0 * i);
            graphics.setColor(weightToColor(mockWeightColor, alpha));
            graphics.drawLine(innerX, innerY, innerX, innerY + 22);
            innerX++;
        }

        // labels
        graphics.setColor(Colors.textWithAlpha(alpha));
        graphics.drawLine(startX, startY, startX, startY + 50);
        graphics.drawLine(startX + 150, startY, startX + 150, startY + 50);
        drawText("-1", startX - 14, startY + 80, alpha);
        drawText("1", startX + 144, startY + 80, alpha);
    }

    public int calculateAppearingAlpha(int startAppearingFrame) {
        if (startAppearingFrame + Settings.CHANGING_SLOW_FRAMES < slowFrame)
            return 255;
        else
            return (int) ((slowFrame - startAppearingFrame) / 15.0 * 255);
    }

    public Color weightToColor(int weight, int alpha) {
        return new Color(weight, 255 - weight, 0, alpha);
    }

    public void drawText(String text, float startX, float startY, int alpha) {
        graphics.setColor(Colors.textWithAlpha(alpha));
        graphics.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        graphics.drawString(text, startX, startY);
    }
}
