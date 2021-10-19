package Visualization;

import NeuralNetwork.Layer;
import NeuralNetwork.Neuron;

import javax.swing.*;
import java.awt.*;

public class NeuralNetworkPanel extends JPanel {
    private final int neuronSize = 40;
    private final int neuronYaxisGap = 60;
    private final int neuronXaxisGap = 50;
    private final int xDistanceBetweenLayers = 150;

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
        Layer layer = new Layer(5, 5);
        Layer layer2 = new Layer(5, 5);

        drawLayer(graphics, layer, 0);
        drawLayer(graphics, layer2, 1);
        drawWeights(graphics, layer, layer2, 0);
    }

    /**
     * The function draws one layer as column of neurons
     *
     * @param graphics   {@link Graphics2D} object that paints all the graphic
     * @param layer      {@link Layer} to draw
     * @param layerIndex index of {@link Layer} in {@link NeuralNetwork}
     */
    public void drawLayer(Graphics2D graphics, Layer layer, int layerIndex) {
        graphics.setStroke(new BasicStroke(3));

        int neuronYPosition = neuronYaxisGap;
        int layerXPosition = neuronXaxisGap + layerIndex * xDistanceBetweenLayers;

        for (int i = 0; i < layer.neurons.length; i++) {
            graphics.drawOval(layerXPosition, neuronYPosition, neuronSize, neuronSize);
            neuronYPosition += neuronYaxisGap;
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
    public void drawWeights(Graphics2D graphics, Layer leftLayer, Layer rightLayer, int leftLayerIndex) {
        graphics.setStroke(new BasicStroke(2));

        int leftNeuronY = neuronYaxisGap;
        int leftLayerX = neuronXaxisGap + leftLayerIndex * xDistanceBetweenLayers;
        int rightLayerX = neuronXaxisGap + (leftLayerIndex + 1) * xDistanceBetweenLayers;

        for (int leftNeuron = 0; leftNeuron < leftLayer.neurons.length; leftNeuron++) {
            int rightNeuronY = neuronYaxisGap;
            for (int rightNeuron = 0; rightNeuron < rightLayer.neurons.length; rightNeuron++) {
                graphics.setColor(weightToColor(rightLayer.neurons[rightNeuron].weights[leftNeuron]));
                drawLine(graphics,
                        weightLineStartingPoint(leftNeuronY, leftLayerX, neuronSize),
                        weightLineEndingPoint(rightNeuronY, rightLayerX, neuronSize));
                rightNeuronY += neuronYaxisGap;
            }
            leftNeuronY += neuronYaxisGap;
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
    public Point weightLineStartingPoint(int leftNeuronY, int leftLayerX, int neuronSize) {
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
    public Point weightLineEndingPoint(int rightNeuronY, int rightLayerX, int neuronSize) {
        return new Point(rightLayerX, rightNeuronY + neuronSize / 2);
    }

    /**
     * Draws line from starting point to ending point;
     *
     * @param graphics {@link Graphics2D} object that paints all the graphic
     * @param starting {@link Point} where the line starts
     * @param ending   {@link Point} where the line ends
     */
    public void drawLine(Graphics2D graphics, Point starting, Point ending) {
        graphics.drawLine(starting.x, starting.y, ending.x, ending.y);
    }

    /**
     * Converts weight to color.
     *
     * @param weight weight of {@link Neuron}
     * @return red if weight is negative blue otherwise
     */
    public Color weightToColor(double weight) {
        return weight >= 0 ? new Color(255, 0, 0) : new Color(0, 0, 255);
    }
}
