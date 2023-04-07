package neat.phenotype;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The class represents connection between {@link NEATNeuron}.
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Connection {
    public NEATNeuron from;
    public NEATNeuron to;
    public double weight;

    public Connection(NEATNeuron from, NEATNeuron to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}
