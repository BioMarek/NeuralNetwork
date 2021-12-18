package Visualizations.DTOs;

import Interfaces.NeuralNetwork;
import lombok.Data;

/**
 * DTO representing connection of two neurons for {@link NeuralNetwork} visualization.
 */
@Data
public class VisConnectionDTO {
    public int from;
    public int to;
    public double weight;

    public VisConnectionDTO(int from, int to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}
