package NeuralNetwork;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * The class represents hidden layer of neural network.
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Layer {
    public Neuron[] neurons;
    public double[] layerOutputs;

    public Layer(int numOfNeuronInPrevLayer, int numOfNeurons) {
        this.neurons = new Neuron[numOfNeurons];
        this.layerOutputs = new double[numOfNeurons];

        for (int i = 0; i < numOfNeurons; i++) {
            neurons[i] = new Neuron(numOfNeuronInPrevLayer);
        }
    }

    /**
     * @return deep copy of {@link Layer}
     */
    public Layer copy() {
        Layer layer = new Layer();
        Neuron[] newNeurons = new Neuron[neurons.length];

        for (int i = 0; i < neurons.length; i++) {
            newNeurons[i] = neurons[i].copy();
        }

        layer.neurons = newNeurons;
        layer.layerOutputs = Arrays.copyOf(this.layerOutputs, this.layerOutputs.length);
        return layer;
    }

    /**
     * Calculates output of layer and saves it into layerOutputs {@link #layerOutputs}.
     *
     * @param prevLayerOutputs The outputs of layer which are used as inputs for this layer.
     */
    public void getOutput(double[] prevLayerOutputs) {
        for (int i = 0; i < neurons.length; i++) {
            layerOutputs[i] = neurons[i].getOutput(prevLayerOutputs);
        }
    }

    /**
     * The function randomly picks given number of {@link Neuron} and changes given number randomly chosen weights.
     *
     * @param numOfNeuronsToMutate number of neurons that should be mutated
     * @param numOfMutations       number of weight changes in each neuron
     */
    public void mutateRandomNeuron(int numOfNeuronsToMutate, int numOfMutations) {
        for (int i = 0; i < numOfNeuronsToMutate; i++) {
            neurons[Util.randomInt(0, neurons.length)].mutateRandomWeight(numOfMutations);
        }
    }

    /**
     * Prints contents of layer in human-readable format.
     */
    public void printLayer() {
        Stream.of(neurons).forEach(System.out::println);
    }
}
