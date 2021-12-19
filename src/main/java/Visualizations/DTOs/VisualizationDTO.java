package Visualizations.DTOs;

import Interfaces.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * DTO used to transfer data for {@link NeuralNetwork} visualization.
 */
public class VisualizationDTO {
    public List<VisLayerDTO> layers = new ArrayList<>();
    public List<VisConnectionDTO> connections = new ArrayList<>();

    /**
     * Used to convert {@link Set<Integer>} of neuron names into {@link List<Integer>} of neuron names.
     */
    public VisualizationDTO build() {
        layers.forEach(VisLayerDTO::build);
        return this;
    }
}
