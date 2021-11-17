package NEAT.Evolution;

import NEAT.NeuronType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NodeGene implements Comparable<NodeGene> {
    public NeuronType type;
    public int name;
    public boolean[] enabled;

    public NodeGene(NeuronType type, boolean[] enabled, int name) {
        this.type = type;
        this.name = name;
        this.enabled = enabled;
    }

    @Override
    public int compareTo(NodeGene nodeGene) {
        if (this.name == nodeGene.name)
            return 0;
        return this.name < nodeGene.name ? -1 : 1;
    }
}
