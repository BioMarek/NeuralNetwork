package Visualizations.DTOs;


import Interfaces.NeuralNetwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DTO representing layer for {@link NeuralNetwork} visualization.
 */
public class VisLayerDTO {
    private final Set<Integer> neuronSet = new HashSet<>();
    public List<Integer> neurons = new ArrayList<>();
    public List<VisConnectionDTO> connections = new ArrayList<>();

    public void addNeuron(Integer neuron) {
        neuronSet.add(neuron);
    }

    public void build() {
        neurons = new ArrayList<>(neuronSet);
    }
}
