package visualizations.nnVisualization;


import neat.phenotype.NeuralNetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO representing layer for {@link NeuralNetwork} visualization.
 */
public class VisLayerDTO {
    private final Map<Integer, VisNeuronDTO> visNeuronDTOMap = new HashMap<>();
    public List<VisNeuronDTO> neurons = new ArrayList<>();

    /**
     * Adds {@link VisNeuronDTO} into layer and returns it. If {@link VisNeuronDTO} with such name exists then the
     * existing one will be returned.
     *
     * @param neuron to be added
     * @return correct neuron either new or the one already in {@link VisLayerDTO}
     */
    public VisNeuronDTO addNeuron(VisNeuronDTO neuron) {
        visNeuronDTOMap.putIfAbsent(neuron.name, neuron);
        return visNeuronDTOMap.get(neuron.name);
    }

    /**
     * Calculates {@link VisNeuronDTO} position, they are sorted by their name so {@link VisNeuronDTO} with lower name
     * will above neuron with {@link VisNeuronDTO} name.
     */
    public void calculateNeuronPositions() {
        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).position = i;
        }
    }

    /**
     * After all neurons are this function creates {@link ArrayList<VisNeuronDTO>} from them, sorts them and calculates
     * their positions.
     */
    public void build() {
        neurons = new ArrayList<>(visNeuronDTOMap.values());
        Collections.sort(neurons);
        calculateNeuronPositions();
    }
}
