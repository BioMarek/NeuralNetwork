package NEAT.Phenotype;


import NEAT.NeuronType;
import Utils.Util;
import lombok.EqualsAndHashCode;

import java.util.function.Function;

@EqualsAndHashCode
public class NEATNeuron {
    public double innerPotential;
    public double bias;
    public int name;
    public NeuronType neuronType;

    public NEATNeuron(int name, NeuronType neuronType) {
        this.innerPotential = Util.randomDouble();
        this.bias = 0;
        this.name = name;
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
}
