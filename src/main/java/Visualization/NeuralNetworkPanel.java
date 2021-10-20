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

    public NeuralNetworkPanel() {
        this.setPreferredSize(new Dimension(500, 500));
        this.setBackground(Color.WHITE);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        drawNeuralNetwork((Graphics2D) g);
    }

    public void drawNeuralNetwork(Graphics2D graphics) {
        NeuralNetwork neuralNetwork = new NeuralNetwork(new int[]{10, 2, 6, 5});

        int[] yAxisOffsets = layersYAxisOffset(neuralNetwork);
        for (int i = 0; i < neuralNetwork.hiddenLayers.size(); i++) {
            drawLayer(graphics, neuralNetwork.hiddenLayers.get(i), i, yAxisOffsets[i]);
        }
        for (int i = 0; i < neuralNetwork.hiddenLayers.size() - 1; i++) {
            drawWeights(graphics, neuralNetwork.hiddenLayers.get(i), neuralNetwork.hiddenLayers.get(i + 1), i, yAxisOffsets);
        }
    }

    /**
     * The function draws one layer as column of neurons
     *
     * @param graphics   {@link Graphics2D} object that paints all the graphic
     * @param layer      {@link Layer} to draw
     * @param layerIndex index of {@link Layer} in {@link NeuralNetwork}
     */
    protected void drawLayer(Graphics2D graphics, Layer layer, int layerIndex, int yAxisOffset) {
        graphics.setStroke(new BasicStroke(3));

        int neuronYPosition = neuronYAxisGap + yAxisOffset;
        int layerXPosition = neuronXAxisGap + layerIndex * xAxisDistanceBetweenLayers;

        for (int i = 0; i < layer.neurons.length; i++) {
            graphics.drawOval(layerXPosition, neuronYPosition, neuronSize, neuronSize);
            neuronYPosition += neuronYAxisGap;
        }
    }

    /**
     * Draws weights between Layer on the "left" the one with smaller index and layer on the "right" the one with
     * higher index.
     *
     * @param graphics       {@link Graphics2D} object that paints all the graphic
     * @param leftLayer      {@link Layer} with the index 'i' in {@link NeuralNetwork}
     * @param rightLayer     {@link Layer} with the index 'i + 1' in {@link NeuralNetwork}
     * @param leftLayerIndex index 'i' of {@link Layer} in {@link NeuralNetwork}
     */
    protected void drawWeights(Graphics2D graphics, Layer leftLayer, Layer rightLayer, int leftLayerIndex, int[] yAxisOffset) {
        graphics.setStroke(new BasicStroke(2));

        int leftNeuronY = neuronYAxisGap + yAxisOffset[leftLayerIndex];
        int leftLayerX = neuronXAxisGap + leftLayerIndex * xAxisDistanceBetweenLayers;
        int rightLayerX = leftLayerX + xAxisDistanceBetweenLayers;

        for (int leftNeuron = 0; leftNeuron < leftLayer.neurons.length; leftNeuron++) {
            int rightNeuronY = neuronYAxisGap + yAxisOffset[leftLayerIndex + 1];
            for (int rightNeuron = 0; rightNeuron < rightLayer.neurons.length; rightNeuron++) {
                graphics.setColor(weightToColor(rightLayer.neurons[rightNeuron].weights[leftNeuron]));
                drawLine(graphics,
                        weightLineStartingPoint(leftNeuronY, leftLayerX, neuronSize),
                        weightLineEndingPoint(rightNeuronY, rightLayerX, neuronSize));
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
     * @param neuronSize  {@link Neuron} size
     * @return {@link Point}
     */
    protected Point weightLineStartingPoint(int leftNeuronY, int leftLayerX, int neuronSize) {
        return new Point(leftLayerX + neuronSize, leftNeuronY + neuronSize / 2);
    }

    /**
     * Calculates weight line starting point based on size of the {@link Neuron} and its position within {@link Layer}.
     *
     * @param rightNeuronY y index of {@link Neuron} upper left corner
     * @param rightLayerX  x index of {@link Neuron} upper left corner
     * @param neuronSize   {@link Neuron} size
     * @return {@link Point}
     */
    protected Point weightLineEndingPoint(int rightNeuronY, int rightLayerX, int neuronSize) {
        return new Point(rightLayerX, rightNeuronY + neuronSize / 2);
    }

    /**
     * Draws line from starting point to ending point;
     *
     * @param graphics {@link Graphics2D} object that paints all the graphic
     * @param starting {@link Point} where the line starts
     * @param ending   {@link Point} where the line ends
     */
    protected void drawLine(Graphics2D graphics, Point starting, Point ending) {
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
                .map(layer -> (layer.neurons.length - 1) * neuronYAxisGap + neuronSize)
                .collect(Collectors.toList());

        return layerHeights.stream()
                .mapToInt(x -> (Collections.max(layerHeights) - x) / 2)
                .toArray();
    }
}
