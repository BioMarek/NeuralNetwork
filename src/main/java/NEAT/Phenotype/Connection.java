package NEAT.Phenotype;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Connection implements Comparable<Connection> {
    public NEATNeuron from;
    public NEATNeuron to;
    public double weight;

    public Connection(NEATNeuron from, NEATNeuron to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public int compareTo(Connection connection) {
        if (this.from.name == connection.from.name)
            return 0;
        return (this.from.name > connection.from.name) ? 1 : -1;
    }
}
