package Visualizations.DTOs;


import Interfaces.NeuralNetwork;

import java.util.*;

/**
 * DTO representing layer for {@link NeuralNetwork} visualization.
 */
public class VisLayerDTO {
    private final Set<VisNeuronDTO> visNeuronDTOSet = new HashSet<>();
    public List<VisNeuronDTO> neurons = new ArrayList<>();
    public List<VisConnectionDTO> connections = new ArrayList<>();

    public void addNeuron(VisNeuronDTO neuron) {
        visNeuronDTOSet.add(neuron);
    }

    public void calculateNeuronPositions() {
        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).layer = i;
        }
    }

    public void build() {
        calculateNeuronPositions();
        neurons = new ArrayList<>(visNeuronDTOSet);
        Collections.sort(neurons);
    }
}
