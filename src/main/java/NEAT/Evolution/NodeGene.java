package NEAT.Evolution;

import NEAT.NeuronType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NodeGene implements Comparable<NodeGene> {
    public final NeuronType type;
    public final int name;
    public List<ConnectionGene> connectionGenes;
    public int layer;

    public NodeGene(NeuronType type, int name) {
        this.type = type;
        this.name = name;
        connectionGenes = new ArrayList<>();
    }

    @Override
    public int compareTo(NodeGene nodeGene) {
        if (this.layer == nodeGene.layer && this.name == nodeGene.name)
            return 0;
        if (this.layer == nodeGene.layer)
            return this.name < nodeGene.name ? -1 : 1;
        return this.layer < nodeGene.layer ? -1 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeGene nodeGene = (NodeGene) o;
        return name == nodeGene.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
