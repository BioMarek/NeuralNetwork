package NEAT.Phenotype;

import Interfaces.NeuralNetwork;
import NEAT.Evolution.GenePool;
import NEAT.NeuronType;
import Utils.Util;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The class represents properties of NEAT network derived particular genotype. Genotypes are stored in {@link GenePool}
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Phenotype implements NeuralNetwork {
    public List<NEATNeuron> inputNeurons;
    public List<NEATNeuron> hiddenNeurons;
    public List<NEATNeuron> outputNeurons;
    public List<Connection> connections;
    public Function<Double, Double> hiddenLayerActivationFunc;
    public Function<Double, Double> outputLayerActivationFunc;
    public int score = 0;

    public Phenotype(List<NEATNeuron> neurons, List<Connection> connections, Function<Double, Double> hiddenLayerActivationFunc, Function<Double, Double> outputLayerActivationFunc) {
        this.inputNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.INPUT).collect(Collectors.toList());
        this.hiddenNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.HIDDEN).collect(Collectors.toList());
        this.outputNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.OUTPUT).collect(Collectors.toList());
        this.connections = connections;
        this.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
        this.outputLayerActivationFunc = outputLayerActivationFunc;
    }

    /**
     * Calculates output of {@link Phenotype} using supplied activation function. Activation function used in input
     * neurons is {@link Util#activationFunctionIdentity()}.
     *
     * @param inputs double array of inputs
     * @return double array of outputs
     */
    @Override
    public double[] getNetworkOutput(double[] inputs) {
        for (int i = 0; i < inputs.length; i++) {
            inputNeurons.get(i).bias = inputs[i];
        }
        for (Connection connection : connections) {
            if (connection.from.neuronType == NeuronType.INPUT) {
                connection.to.innerPotential += connection.from.getOutput(Util.activationFunctionIdentity()) * connection.weight;
            } else
                connection.to.innerPotential += connection.from.getOutput(hiddenLayerActivationFunc) * connection.weight;
        }

        return Util.primitiveDoubleArrayFromList(
                outputNeurons.stream()
                        .map(neatNeuron -> neatNeuron.getOutput(outputLayerActivationFunc))
                        .collect(Collectors.toList()));
    }

    /**
     * Increases {@link Phenotype} score by given amount.
     *
     * @param amount number that will be added to score
     */
    @Override
    public void increaseScore(int amount) {
        score += amount;
    }

    /**
     * Prints neural network in human-readable format.
     */
    @Override
    public void printNetwork() {
        for (Connection connection : connections) {
            System.out.println(connection.from.name + " -> " + connection.to.name);
        }
    }
}
