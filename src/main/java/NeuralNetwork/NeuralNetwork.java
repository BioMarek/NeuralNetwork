package NeuralNetwork;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@link NeuralNetwork} is composed of {@link Layer}. Hidden layers compute result based on input supplied into
 * first layer. Results of computations are propagated into subsequent layers up to last layer which output is final
 * output of {@link NeuralNetwork}.
 */
@EqualsAndHashCode
public class NeuralNetwork implements Comparable<NeuralNetwork> {
    public final List<Layer> hiddenLayers;
    public Function<Double, Double> hiddenLayerActivationFunc;
    public Function<Double, Double> outputLayerActivationFunc;
    public int score;
    public String name;

    /**
     * @param sizes                     Array of sizes, first number is number of {@link NeuralNetwork} inputs. Numbers
     *                                  size[1]...size[n] say how many {@link Neuron} should be in n-th hidden {@link Layer}.
     * @param hiddenLayerActivationFunc activation function used in hidden layers
     * @param outputLayerActivationFunc activation function used in output layer
     */
    public NeuralNetwork(int[] sizes, Function<Double, Double> hiddenLayerActivationFunc, Function<Double, Double> outputLayerActivationFunc) {
        this.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
        this.outputLayerActivationFunc = outputLayerActivationFunc;

        if (sizes.length < 1)
            throw new IllegalArgumentException("Neural network needs at least 1 hidden layer");

        hiddenLayers = new ArrayList<>();
        for (int i = 0; i < sizes.length - 1; i++) {
            hiddenLayers.add(new Layer(sizes[i], sizes[i + 1], i));
        }
    }

    public NeuralNetwork(List<Layer> hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
    }

    /**
     * @return deep copy of {@link NeuralNetwork}
     */
    public NeuralNetwork copy() {
        return new NeuralNetwork(
                hiddenLayers.stream()
                        .map(x -> x.copy())
                        .collect(Collectors.toList())
        );
    }

    /**
     * The function takes input vector and feeds it into {@link NeuralNetwork}. Results of {@link Layer} is fed as input
     * into next and result of last {@link Layer is presented as output of {@link NeuralNetwork} computation.
     *
     * @param inputs to evaluate
     * @return vector of values copmuted by {@link NeuralNetwork}
     */
    public double[] getNetworkOutput(double[] inputs) {
        int outputLayerIndex = hiddenLayers.size() - 1;
        hiddenLayers.get(0).calculateOutput(inputs, hiddenLayerActivationFunc);

        for (int i = 1; i < hiddenLayers.size() - 1; i++) {
            hiddenLayers.get(i).calculateOutput(
                    hiddenLayers.get(i - 1).layerOutputs,
                    hiddenLayerActivationFunc);
        }
        hiddenLayers.get(outputLayerIndex).calculateOutput(
                hiddenLayers.get(outputLayerIndex - 1).layerOutputs,
                outputLayerActivationFunc);

        return hiddenLayers.get(outputLayerIndex).layerOutputs;
    }

    /**
     * The function mutates all hidden layers.
     *
     * @param numOfNeuronsToMutate number of neurons that should be mutated in each layer
     * @param numOfMutations       number of weight changes in each neuron
     */
    public void mutateLayers(int numOfNeuronsToMutate, int numOfMutations) {
        hiddenLayers.forEach((layer -> layer.mutateRandomNeuron(numOfNeuronsToMutate, numOfMutations)));
    }

    /**
     * Prints neural network in human-readable format.
     */
    public void printNeuralNetwork() {
        for (int i = 0; i < hiddenLayers.size(); i++) {
            System.out.println("Layer: " + i);
            hiddenLayers.get(i).printLayer();
        }
    }

    @Override
    public int compareTo(NeuralNetwork neuralNetwork) {
        if (this.score == neuralNetwork.score)
            return 0;
        return (this.score > neuralNetwork.score) ? 1 : -1;
    }
}
