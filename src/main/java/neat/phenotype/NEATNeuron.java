package neat.phenotype;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import neat.NeuronType;

import java.util.function.Function;

/**
 * The class represents {@link NEATNeuron} which represents node of {@link Phenotype}.
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NEATNeuron {
    public double innerPotential;
    public double bias;
    public int name;
    public int layer;
    public NeuronType neuronType;

    public NEATNeuron(int name, int layer, NeuronType neuronType) {
        this.innerPotential = 0;
        this.bias = 0;
        this.name = name;
        this.layer = layer;
        this.neuronType = neuronType;
    }

    public NEATNeuron(double innerPotential, double bias, int name, NeuronType neuronType) {
        this.innerPotential = innerPotential;
        this.bias = bias;
        this.name = name;
        this.neuronType = neuronType;
    }

    double getOutput(Function<Double, Double> activationFunction) {
        return activationFunction.apply(bias + innerPotential);
    }

    public void reset() {
        innerPotential = 0;
    }

    @Override
    public String toString() {
        return "NEATNeuron{" +
                "innerPotential=" + innerPotential +
                ", bias=" + bias +
                ", name=" + name +
                ", neuronType=" + neuronType +
                '}';
    }
}
