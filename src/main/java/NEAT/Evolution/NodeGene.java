package NEAT.Evolution;

import NEAT.NeuronType;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class NodeGene implements Comparable<NodeGene> {
    public final NeuronType type;
    public final int name;

    public NodeGene(NeuronType type, int name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public int compareTo(NodeGene nodeGene) {
        if (this.name == nodeGene.name)
            return 0;
        return this.name < nodeGene.name ? -1 : 1;
    }
}
