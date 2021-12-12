package NEAT.Evolution;

import NEAT.Phenotype.Connection;
import NEAT.Phenotype.Phenotype;

import java.util.Objects;

/**
 * The class represents gene which describes how {@link Connection} in {@link Phenotype} should look like.
 */
public class ConnectionGene implements Comparable<ConnectionGene> {
    public final NodeGene from;
    public final NodeGene to;
    public double weight;
    public boolean enabled;

    public ConnectionGene(NodeGene from, NodeGene to, double weight, boolean enabled) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.enabled = enabled;
    }

    public ConnectionGene copy() {
        return new ConnectionGene(from, to, weight, enabled);
    }

    /**
     * Orders {@link Connection}s both "from" names and "to" names are sorted in descending order.
     *
     * @param connectionGene to compare with this {@link Connection}
     * @return 0 if connections are equal, 1 if this {@link Connection} has higher "from" or "to" name, -1 otherwise.
     */
    @Override //TODO try better solution
    public int compareTo(ConnectionGene connectionGene) {
        if (this.from.layer == connectionGene.from.layer && this.from.name == connectionGene.from.name && this.to.name == connectionGene.to.name)
            return 0;
        if (this.from.layer == connectionGene.from.layer && this.from.name == connectionGene.from.name)
            return (this.to.name > connectionGene.to.name) ? 1 : -1;
        if (this.from.layer == connectionGene.from.layer)
            return (this.from.name > connectionGene.from.name) ? 1 : -1;
        return (this.from.layer > connectionGene.from.layer) ? 1 : -1;
    }

    public void printConnectionGene() {
        System.out.printf("%-3d -> %-4d %7.4f%n", from.name, to.name, weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionGene that = (ConnectionGene) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
