package NeuralNetwork;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The class represents hidden layer of neural network.
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Layer {
    public Neuron[] neurons;
    public double[] layerOutputs;
    public int index;
    public int length;

    public Layer(int numOfNeuronInPrevLayer, int numOfNeurons, int index) {
        this.neurons = new Neuron[numOfNeurons];
        this.layerOutputs = new double[numOfNeurons];
        this.index = index;
        this.length = numOfNeurons;

        for (int i = 0; i < numOfNeurons; i++) {
            neurons[i] = new Neuron(numOfNeuronInPrevLayer);
        }
    }

    /**
     * @return deep copy of {@link Layer}
     */
    public Layer copy() {
        Layer layer = new Layer();
        Neuron[] newNeurons = new Neuron[length];

        for (int i = 0; i < length; i++) {
            newNeurons[i] = neurons[i].copy();
        }

        layer.neurons = newNeurons;
        layer.layerOutputs = Arrays.copyOf(this.layerOutputs, this.layerOutputs.length);
        layer.index = this.index;
        layer.length = this.length;
        return layer;
    }

    /**
     * Calculates output of layer and saves it into layerOutputs {@link #layerOutputs}.
     *
     * @param prevLayerOutputs   the outputs of layer which are used as inputs for this layer.
     * @param activationFunction function used to calculate outputs
     */
    public void calculateOutput(double[] prevLayerOutputs, Function<Double, Double> activationFunction) {
        for (int i = 0; i < length; i++) {
            layerOutputs[i] = neurons[i].getOutput(prevLayerOutputs, activationFunction);
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
            neurons[Util.randomInt(0, length)].mutateRandomWeight(numOfMutations);
        }
    }

    /**
     * Prints contents of layer in human-readable format.
     */
    public void printLayer() {
        Stream.of(neurons).forEach(System.out::println);
    }
}
