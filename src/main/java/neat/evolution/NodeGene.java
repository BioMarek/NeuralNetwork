package neat.evolution;

import neat.NeuronType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NodeGene implements Comparable<NodeGene> {
    public final NeuronType type;
    public final int name;
    public int layer;
    public List<ConnectionGene> connectionGenes = new ArrayList<>();

    public NodeGene(NeuronType type, int name, int layer) {
        this.type = type;
        this.name = name;
        this.layer = layer;
    }

    public NodeGene copy() {
        // connections have to be added in genotype because they are created there
        return new NodeGene(this.type, this.name, this.layer);
    }

    @Override
    public int compareTo(NodeGene nodeGene) {
        if (this.layer == nodeGene.layer && this.name == nodeGene.name)
            return 0;
        if (this.layer == nodeGene.layer)
            return this.name < nodeGene.name ? -1 : 1;
        return this.layer < nodeGene.layer ? -1 : 1;
    }

    /**
     * In the copy we want two {@link Genotype} to have same node names and {@link ConnectionGene}s going from
     * {@link NodeGene} with same node into {@link NodeGene} with same name. But we cannot compare lists of
     * {@link ConnectionGene}s because it would cause stack overflow.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeGene nodeGene = (NodeGene) o;
        return name == nodeGene.name && layer == nodeGene.layer && type == nodeGene.type && connectionGenes.equals(nodeGene.connectionGenes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, layer, connectionGenes);
    }

    @Override
    public String toString() {
        StringBuilder connections = new StringBuilder();
        for (ConnectionGene connectionGene : connectionGenes) {
            connections.append(" ").append(connectionGene.to.name);
        }
        return "{" + name + " " + type + " " + layer + " Connections to:" + connections + "}";
    }
}
