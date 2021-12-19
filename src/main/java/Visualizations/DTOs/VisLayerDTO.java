package Visualizations.DTOs;


import Interfaces.NeuralNetwork;

import java.util.*;

/**
 * DTO representing layer for {@link NeuralNetwork} visualization.
 */
public class VisLayerDTO {
    private final Map<Integer, VisNeuronDTO> visNeuronDTOMap = new HashMap<>();
    public List<VisNeuronDTO> neurons = new ArrayList<>();

    public VisNeuronDTO checkNeuronExistence(VisNeuronDTO neuron) {
        // TODO refactor
        if (visNeuronDTOMap.get(neuron.name) == null) {
            visNeuronDTOMap.put(neuron.name, neuron);
            return neuron;
        } else return visNeuronDTOMap.get(neuron.name);
    }

    public void calculateNeuronPositions() {
        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).position = i;
        }
    }

    public void build() {
        neurons = new ArrayList<>(visNeuronDTOMap.values());
        Collections.sort(neurons);
        calculateNeuronPositions();
    }
}
