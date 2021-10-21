package Visualization;

import NeuralNetwork.Layer;
import NeuralNetwork.NeuralNetwork;
import NeuralNetwork.Neuron;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NeuralNetworkPanel extends JPanel {
    private final int neuronSize = 40;
    private final int neuronYAxisGap = 60;
    private final int neuronXAxisGap = 50;
    private final int xAxisDistanceBetweenLayers = 150;
    private final int weightStroke = 2;
    private final int neuronStroke = 3;

    private Graphics2D graphics;

    public NeuralNetworkPanel() {
        this.setPreferredSize(new Dimension(500, 500));
        this.setBackground(Color.WHITE);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawNeuralNetwork();
    }

    public void drawNeuralNetwork() {
        NeuralNetwork neuralNetwork = new NeuralNetwork(new int[]{10, 2, 6, 5});

        int[] yAxisOffsets = layersYAxisOffset(neuralNetwork);
        for (int i = 0; i < neuralNetwork.hiddenLayers.size(); i++) {
            drawLayer(neuralNetwork.hiddenLayers.get(i), i, yAxisOffsets[i]);
        }
        for (int i = 0; i < neuralNetwork.hiddenLayers.size() - 1; i++) {
            drawWeights(neuralNetwork.hiddenLayers.get(i), neuralNetwork.hiddenLayers.get(i + 1), yAxisOffsets);
        }
    }

    /**
     * The function draws one layer as column of neurons
     *
     * @param layer      {@link Layer} to draw
     * @param layerIndex index of {@link Layer} in {@link NeuralNetwork}
     */
    protected void drawLayer(Layer layer, int layerIndex, int yAxisOffset) {
        graphics.setStroke(new BasicStroke(neuronStroke));

        int neuronYPosition = neuronYAxisGap + yAxisOffset;
        int layerXPosition = neuronXAxisGap + layerIndex * xAxisDistanceBetweenLayers;

        for (int i = 0; i < layer.length; i++) {
            graphics.drawOval(layerXPosition, neuronYPosition, neuronSize, neuronSize);
            neuronYPosition += neuronYAxisGap;
        }
    }

    /**
     * Draws weights between Layer on the "left" the one with smaller index and layer on the "right" the one with
     * higher index.
     *
     * @param leftLayer      {@link Layer} with the index 'i' in {@link NeuralNetwork}
     * @param rightLayer     {@link Layer} with the index 'i + 1' in {@link NeuralNetwork}
     */
    protected void drawWeights(Layer leftLayer, Layer rightLayer, int[] yAxisOffset) {
        graphics.setStroke(new BasicStroke(weightStroke));

        int leftNeuronY = neuronYAxisGap + yAxisOffset[leftLayer.index];
        int leftLayerX = neuronXAxisGap + leftLayer.index * xAxisDistanceBetweenLayers;
        int rightLayerX = leftLayerX + xAxisDistanceBetweenLayers;

        for (int leftNeuron = 0; leftNeuron < leftLayer.length; leftNeuron++) {
            int rightNeuronY = neuronYAxisGap + yAxisOffset[rightLayer.index];
            for (int rightNeuron = 0; rightNeuron < rightLayer.length; rightNeuron++) {
                graphics.setColor(weightToColor(rightLayer.neurons[rightNeuron].weights[leftNeuron]));
                drawLine(weightStartingPoint(leftNeuronY, leftLayerX), weightEndingPoint(rightNeuronY, rightLayerX));
                rightNeuronY += neuronYAxisGap;
            }
            leftNeuronY += neuronYAxisGap;
        }
    }

    /**
     * Calculates weight line starting point based on size of the {@link Neuron} and its position within {@link Layer}.
     *
     * @param leftNeuronY y index of {@link Neuron} upper left corner
     * @param leftLayerX  x index of {@link Neuron} upper left corner
     * @return {@link Point}
     */
    protected Point weightStartingPoint(int leftNeuronY, int leftLayerX) {
        return new Point(leftLayerX + neuronSize, leftNeuronY + neuronSize / 2);
    }

    /**
     * Calculates weight line starting point based on size of the {@link Neuron} and its position within {@link Layer}.
     *
     * @param rightNeuronY y index of {@link Neuron} upper left corner
     * @param rightLayerX  x index of {@link Neuron} upper left corner
     * @return {@link Point}
     */
    protected Point weightEndingPoint(int rightNeuronY, int rightLayerX) {
        return new Point(rightLayerX, rightNeuronY + neuronSize / 2);
    }

    /**
     * Draws line from starting point to ending point;
     *
     * @param starting {@link Point} where the line starts
     * @param ending   {@link Point} where the line ends
     */
    protected void drawLine(Point starting, Point ending) {
        graphics.drawLine(starting.x, starting.y, ending.x, ending.y);
    }

    /**
     * Converts weight to color.
     *
     * @param weight weight of {@link Neuron}
     * @return red if weight is negative blue otherwise
     */
    protected Color weightToColor(double weight) {
        return weight < 0 ? new Color(255, 0, 0) : new Color(0, 0, 255);
    }

    /**
     * The function calculates offset on y axis so that {@link Layer}s with different number of {@link Neuron} are centered.
     *
     * @param neuralNetwork which {@link Layer}s will be evaluated
     * @return array of offsets for each layer
     */
    protected int[] layersYAxisOffset(NeuralNetwork neuralNetwork) {
        List<Integer> layerHeights = neuralNetwork.hiddenLayers.stream()
                .map(layer -> (layer.length - 1) * neuronYAxisGap + neuronSize)
                .collect(Collectors.toList());

        return layerHeights.stream()
                .mapToInt(x -> (Collections.max(layerHeights) - x) / 2)
                .toArray();
    }
}
