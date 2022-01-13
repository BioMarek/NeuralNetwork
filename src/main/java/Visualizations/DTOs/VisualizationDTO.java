package Visualizations.DTOs;

import Interfaces.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Utils.Util.repeat;

/**
 * DTO used to transfer data for {@link NeuralNetwork} visualization.
 */
public class VisualizationDTO {
    public List<VisLayerDTO> layers = new ArrayList<>();
    public List<VisConnectionDTO> connections = new ArrayList<>();

    public VisualizationDTO(int numOfLayers) {
        repeat.accept(numOfLayers, () -> layers.add(new VisLayerDTO()));
    }

    /**
     * Used to convert {@link Map<Integer>} of neuron names into {@link List<VisNeuronDTO>} of neurons.
     */
    public VisualizationDTO build() {
        layers.forEach(VisLayerDTO::build);
        return this;
    }
}
