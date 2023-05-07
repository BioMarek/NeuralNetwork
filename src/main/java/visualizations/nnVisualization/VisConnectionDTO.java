package visualizations.nnVisualization;

import lombok.Data;
import neat.phenotype.NeuralNetwork;

/**
 * DTO representing connection of two neurons for {@link NeuralNetwork} visualization.
 */
@Data
public class VisConnectionDTO {
    public VisNeuronDTO from;
    public VisNeuronDTO to;
    public double weight;

    public VisConnectionDTO(VisNeuronDTO from, VisNeuronDTO to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}
