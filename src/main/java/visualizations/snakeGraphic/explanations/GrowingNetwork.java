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
    private double chanceToDrawWeight = 0.5d;
    private int[][] weights = new int[8][4];
    private int hiddenLayerNodes;
    private List<HiddenLayerConnection> hiddenLayerConnections;

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

        initHiddenLayerConnections();
    }

    public void drawGrowingNetwork(int startAppearingFrame) {
        if (slowFrame > startAppearingFrame) {
            networkScale = Math.min(networkScale + 0.01f, 0.3f);
            startX = startX != endX ? startX + xShift : endX;
            startY = startY != endY ? startY + yShift : endY;

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
        drawHiddenLayerConnections(startX, startY, nodeSize, nodeGap, startAppearingFrame);
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

    public void drawHiddenLayerConnections(int startX, int startY, int nodeSize, int nodeGap, int startAppearingFrame) {
        var alpha = calculateAppearingAlpha(startAppearingFrame);
        for (HiddenLayerConnection hiddenLayerConnection : hiddenLayerConnections) {
            int hiddenLayerShift, outputLayerShift, leftX, leftY, rightX, rightY;
            hiddenLayerShift = startY + nodeGap * (8 - hiddenLayerNodes) / 2;
            outputLayerShift = startY + nodeGap * 2;
            if (hiddenLayerConnection.startLayer == 0) {
                leftX = startX + nodeSize;
                leftY = startY + nodeSize / 2 + hiddenLayerConnection.fromNeuron * nodeGap;
                rightX = startX + nodeSize * 4 * (hiddenLayerConnection.startLayer + 1);
                rightY = hiddenLayerShift + nodeSize / 2 + hiddenLayerConnection.toNeuron * nodeGap;
            } else {
                leftX = startX + nodeSize + nodeSize * 4 * hiddenLayerConnection.startLayer;
                leftY = hiddenLayerShift + nodeSize / 2 + hiddenLayerConnection.fromNeuron * nodeGap;
                rightX = startX + nodeSize * 4 * (hiddenLayerConnection.startLayer + 1);
                rightY = outputLayerShift + nodeSize / 2 + hiddenLayerConnection.toNeuron * nodeGap;
            }

            graphics.setColor(weightToColor(hiddenLayerConnection.weight, alpha));
            graphics.drawLine(leftX, leftY, rightX, rightY);
            graphics.setStroke(new BasicStroke(3f));
        }
    }

    public Color weightToColor(int weight, int alpha) {
        if (weight == 0)
            alpha = 0;
        return new Color(weight, 255 - weight, 0, alpha);
    }

    public void initHiddenLayerConnections() {
        hiddenLayerConnections = new ArrayList<>();
        for (int i = 0; i < hiddenLayerNodes; i++) {
            for (int j = 0; j < 2; j++) {
                int fromNeuron = Util.randomInt(0, 8);
                int toNeuron = Util.randomInt(0, hiddenLayerNodes);
                int weight = Util.randomInt(0, 255);
                hiddenLayerConnections.add(new HiddenLayerConnection(fromNeuron, toNeuron, 0, weight));
            }

            for (int j = 0; j < 2; j++) {
                int fromNeuron = Util.randomInt(0, hiddenLayerNodes);
                int toNeuron = Util.randomInt(0, 4);
                int weight = Util.randomInt(0, 255);
                hiddenLayerConnections.add(new HiddenLayerConnection(fromNeuron, toNeuron, 1, weight));
            }
        }
    }

    public static class HiddenLayerConnection {
        public HiddenLayerConnection(int fromNeuron, int toNeuron, int startLayer, int weight) {
            this.fromNeuron = fromNeuron;
            this.toNeuron = toNeuron;
            this.startLayer = startLayer;
            this.weight = weight;
        }

        public int fromNeuron;
        public int toNeuron;
        public int startLayer;
        public int weight;
    }
}
