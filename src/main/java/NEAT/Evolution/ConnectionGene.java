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

    @Override
    public String toString() {
       return String.format("%-3d -> %-4d %d -> %d %7.4f%n", from.name, to.name, from.layer, to.layer, weight);
    }

    /**
     * {@link NodeGene}s in {@link Connection} are considered same if they have same names. Comparing whole objects
     * would cause stack overflow because references are cyclic.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionGene that = (ConnectionGene) o;
        return Double.compare(that.weight, weight) == 0 && enabled == that.enabled && from.name == that.from.name && to.name == that.to.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from.name, to.name, weight, enabled);
    }
}
