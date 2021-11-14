package NEAT.Phenotype;

import NEAT.Evolution.GenePool;
import NEAT.NeuronType;
import Utils.Util;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The class represents properties of NEAT network derived particular genotype. Genotypes are stored in {@link GenePool}
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Phenotype {
    public List<NEATNeuron> inputNeurons;
    public List<NEATNeuron> hiddenNeurons;
    public List<NEATNeuron> outputNeurons;
    public List<NEATNeuron> neurons;
    public List<Connection> connections;

    public Phenotype(List<NEATNeuron> neurons) {
        this.neurons = neurons;
        this.inputNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.INPUT).collect(Collectors.toList());
        this.hiddenNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.HIDDEN).collect(Collectors.toList());
        this.outputNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.OUTPUT).collect(Collectors.toList());
        connections = new ArrayList<>();
    }

    /**
     * Calculates output of phenotype using supplied activation function. Activation function used in input neurons is
     * {@link Util#activationFunctionIdentity()}.
     *
     * @param inputs                    double array of inputs
     * @param hiddenLayerActivationFunc activation function used in hidden layer
     * @param outputLayerActivationFunc activation function used in output layer
     * @return double array of outputs
     */
    public double[] getOutput(double[] inputs, Function<Double, Double> hiddenLayerActivationFunc, Function<Double, Double> outputLayerActivationFunc) {
        for (int i = 0; i < inputs.length; i++) {
            inputNeurons.get(i).bias = inputs[i];
        }
        for (Connection connection : connections) {
            if (connection.from.neuronType == NeuronType.INPUT) {
                connection.to.innerPotential += connection.from.getOutput(Util.activationFunctionIdentity()) * connection.weight;
            } else
                connection.to.innerPotential += connection.from.getOutput(hiddenLayerActivationFunc) * connection.weight;
        }

        double[] result = new double[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            result[i] = outputNeurons.get(i).getOutput(outputLayerActivationFunc);
        }
        return result;
    }
}
