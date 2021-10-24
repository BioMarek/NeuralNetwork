package NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The {@link NeuralNetwork} is composed of {@link Layer}. Hidden layers compute result based on input supplied into
 * first layer. Results of computations are propagated into subsequent layers up to last layer which output is final
 * output of {@link NeuralNetwork}.
 */
public class NeuralNetwork implements Comparable<NeuralNetwork> {
    public final List<Layer> hiddenLayers;
    public int score;
    public String name;

    /**
     * @param sizes Array of sizes, first number is number of {@link NeuralNetwork} inputs. Numbers size[1]...size[n]
     *              say how many {@link Neuron} should be in n-th hidden {@link Layer}.
     */
    public NeuralNetwork(int[] sizes) {
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
        hiddenLayers.get(0).getOutput(inputs);

        for (int i = 1; i < hiddenLayers.size(); i++) {
            hiddenLayers.get(1).getOutput(hiddenLayers.get(0).layerOutputs);
        }

        return hiddenLayers.get(hiddenLayers.size() - 1).layerOutputs;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeuralNetwork that = (NeuralNetwork) o;
        return Objects.equals(hiddenLayers, that.hiddenLayers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hiddenLayers);
    }
}
