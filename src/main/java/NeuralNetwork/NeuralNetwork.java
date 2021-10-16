package NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link NeuralNetwork} is composed of {@link Layer}. Hidden layers compute result based on input supplied into
 * first layer. Results of computations are propagated into subsequent layers up to last layer which output is final
 * output of {@link NeuralNetwork}.
 */
public class NeuralNetwork {
    private final List<Layer> hiddenLayers;

    public NeuralNetwork(int[] sizes) {
        this.hiddenLayers = new ArrayList<>();

        if (sizes.length > 1) {
            for (int i = 1; i < sizes.length; ++i) {
                this.hiddenLayers.add(new Layer(sizes[i - 1], sizes[i]));
            }
        }
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
        for (Layer layer : hiddenLayers) {
            layer.mutateRandomNeuron(numOfNeuronsToMutate, numOfMutations);
        }
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
}
