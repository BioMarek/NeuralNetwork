package NEAT.Evolution;

import NEAT.Phenotype.Connection;
import NEAT.Phenotype.Phenotype;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The class represents gene which describes how {@link Connection} in {@link Phenotype} should look like.
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionGene {
    public NodeGene from;
    public NodeGene to;
    public double[] weight;
    public boolean[] enabled;

    public ConnectionGene(NodeGene from, NodeGene to, double[] weight, boolean[] enabled) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.enabled = enabled;
    }
}
