package basic_neural_network.neural_network;

import utils.Util;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The class represents hidden layer of neural network.
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Layer implements Serializable {
    public BasicNeuron[] basicNeurons;
    public double[] layerOutputs;
    public int index;
    public int length;

    public Layer(int numOfNeuronInPrevLayer, int numOfNeurons, int index) {
        this.basicNeurons = new BasicNeuron[numOfNeurons];
        this.layerOutputs = new double[numOfNeurons];
        this.index = index;
        this.length = numOfNeurons;

        for (int i = 0; i < numOfNeurons; i++) {
            basicNeurons[i] = new BasicNeuron(numOfNeuronInPrevLayer);
        }
    }

    /**
     * @return deep copy of {@link Layer}
     */
    public Layer copy() {
        Layer layer = new Layer();
        BasicNeuron[] newBasicNeurons = new BasicNeuron[length];

        for (int i = 0; i < length; i++) {
            newBasicNeurons[i] = basicNeurons[i].copy();
        }

        layer.basicNeurons = newBasicNeurons;
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
            layerOutputs[i] = basicNeurons[i].getOutput(prevLayerOutputs, activationFunction);
        }
    }

    /**
     * The function randomly picks given number of {@link BasicNeuron} and changes given number randomly chosen weights.
     *
     * @param numOfNeuronsToMutate number of neurons that should be mutated
     * @param numOfMutations       number of weight changes in each neuron
     */
    public void mutateRandomNeuron(int numOfNeuronsToMutate, int numOfMutations) {
        for (int i = 0; i < numOfNeuronsToMutate; i++) {
            basicNeurons[Util.randomInt(0, length)].mutateRandomWeight(numOfMutations);
        }
    }

    /**
     * Prints contents of layer in human-readable format.
     */
    public void printLayer() {
        Stream.of(basicNeurons).forEach(System.out::println);
    }
}
