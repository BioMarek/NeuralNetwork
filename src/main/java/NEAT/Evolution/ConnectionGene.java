package NEAT.Evolution;

import NEAT.Phenotype.Connection;
import NEAT.Phenotype.Phenotype;
import lombok.EqualsAndHashCode;

/**
 * The class represents gene which describes how {@link Connection} in {@link Phenotype} should look like.
 */
@EqualsAndHashCode
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

    public ConnectionGene copy(){
        return new ConnectionGene(from, to, weight, enabled);
    }

    /**
     * Orders {@link Connection}s both "from" names and "to" names are sorted in descending order.
     *
     * @param connectionGene to compare with this {@link Connection}
     * @return 0 if connections are equal, 1 if this {@link Connection} has higher "from" or "to" name, -1 otherwise.
     */
    @Override
    public int compareTo(ConnectionGene connectionGene) {
        if (this.from.name == connectionGene.from.name && this.to.name == connectionGene.to.name)
            return 0;
        if (this.from.name == connectionGene.from.name)
            return (this.to.name > connectionGene.to.name) ? 1 : -1;
        return (this.from.name > connectionGene.from.name) ? 1 : -1;
    }

    public void printConnectionGene() {
        System.out.println(from.name + " -> " + to.name);
    }
}
