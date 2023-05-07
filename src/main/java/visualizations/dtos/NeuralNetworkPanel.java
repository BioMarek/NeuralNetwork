package visualizations.dtos;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.List;

public class NeuralNetworkPanel extends JPanel {
    private final int neuronSize = 40;
    private final int neuronYAxisGap = 60;
    private final int neuronXAxisGap = 50;
    private final int xAxisDistanceBetweenLayers = 150;
    private final int weightStroke = 2;
    private final int neuronStroke = 3;
    private final VisualizationDTO visualizationDTO;

    private Graphics2D graphics;

    public NeuralNetworkPanel(VisualizationDTO visualizationDTO) {
        this.visualizationDTO = visualizationDTO;
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
        int[] yAxisOffsets = layersYAxisOffset(visualizationDTO);
        for (int i = 0; i < visualizationDTO.layers.size(); i++) {
            drawLayer(visualizationDTO.layers.get(i), i, yAxisOffsets[i]);
        }
        drawWeights(yAxisOffsets);
    }

    /**
     * The function draws one layer as column of neurons
     *
     * @param layer      Layer to draw
     * @param layerIndex index of Layer in BasicNeuralNetwork
     */
    protected void drawLayer(VisLayerDTO layer, int layerIndex, int yAxisOffset) {
        graphics.setStroke(new BasicStroke(neuronStroke));

        int neuronYPosition = neuronYAxisGap + yAxisOffset;
        int layerXPosition = neuronXAxisGap + layerIndex * xAxisDistanceBetweenLayers;

        for (int i = 0; i < layer.neurons.size(); i++) {
            graphics.drawOval(layerXPosition, neuronYPosition, neuronSize, neuronSize);
            neuronYPosition += neuronYAxisGap;
        }
    }

    /**
     * Draws weights between Layer on the "left" the one with smaller index and layer on the "right" the one with
     * higher index.
     *
     */
    protected void drawWeights(int[] yAxisOffset) {
        graphics.setStroke(new BasicStroke(weightStroke));

        for (VisConnectionDTO connection : visualizationDTO.connections) {
            int fromNeuronX = neuronXAxisGap + connection.from.layer * xAxisDistanceBetweenLayers;
            int fromNeuronY = neuronYAxisGap * (connection.from.position + 1) + yAxisOffset[connection.from.layer];
            int toNeuronX = neuronXAxisGap + connection.to.layer * xAxisDistanceBetweenLayers;
            int toNeuronY = neuronYAxisGap * (connection.to.position + 1) + yAxisOffset[connection.to.layer];
            drawLine(weightStartingPoint(fromNeuronY, fromNeuronX), weightEndingPoint(toNeuronY, toNeuronX));
        }
    }

    /**
     * Calculates weight line starting point based on size of the BasicNeuron and its position within Layer.
     *
     * @param leftNeuronY y index of BasicNeuron upper left corner
     * @param leftLayerX  x index of BasicNeuron upper left corner
     * @return {@link Point}
     */
    protected Point weightStartingPoint(int leftNeuronY, int leftLayerX) {
        return new Point(leftLayerX + neuronSize, leftNeuronY + neuronSize / 2);
    }

    /**
     * Calculates weight line starting point based on size of the BasicNeuron and its position within  Layer.
     *
     * @param rightNeuronY y index of BasicNeuron upper left corner
     * @param rightLayerX  x index of  BasicNeuron upper left corner
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
     * @param weight weight of BasicNeuron
     * @return red if weight is negative blue otherwise
     */
    protected Color weightToColor(double weight) {
        return weight < 0 ? new Color(255, 0, 0) : new Color(0, 0, 255);
    }

    /**
     * The function calculates offset on y axis so that layers with different number of BasicNeuron are centered.
     *
     * @param visualizationDTO which {@link VisLayerDTO}s will be evaluated
     * @return array of offsets for each layer
     */
    protected int[] layersYAxisOffset(VisualizationDTO visualizationDTO) {
        List<Integer> layerHeights = visualizationDTO.layers.stream()
                .map(layer -> (layer.neurons.size() - 1) * neuronYAxisGap + neuronSize)
                .toList();

        return layerHeights.stream()
                .mapToInt(x -> (Collections.max(layerHeights) - x) / 2)
                .toArray();
    }
}
