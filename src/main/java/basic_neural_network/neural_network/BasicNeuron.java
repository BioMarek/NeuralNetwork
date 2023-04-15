package basic_neural_network.neural_network;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import utils.Util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Function;

/**
 * The class represents {@link BasicNeuron} which is main building block of {@link Layer}.
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicNeuron implements Serializable {
    public double[] weights;
    public double bias;

    public BasicNeuron(int numOfNeuronInPrevLayer) {
        weights = Util.randomDoubleArray(numOfNeuronInPrevLayer);
        bias = 0;
    }

    /**
     * @return deep copy of {@link BasicNeuron}
     */
    public BasicNeuron copy() {
        BasicNeuron basicNeuron = new BasicNeuron();
        basicNeuron.weights = Arrays.copyOf(this.weights, this.weights.length);
        basicNeuron.bias = this.bias;

        return basicNeuron;
    }

    /**
     * Calculates output of neuron using supplied activation function.
     *
     * @param prevLayerOutputs   the outputs of layer which are used as inputs for layer containing this neuron
     * @param activationFunction function used to calculate output
     * @return result of activation function applied to sum of multiples of weights and inputs
     */
    public double getOutput(double[] prevLayerOutputs, Function<Double, Double> activationFunction) {
        double result = bias;
        for (int i = 0; i < weights.length; i++) {
            result += (weights[i] * prevLayerOutputs[i]);
        }
        return activationFunction.apply(result);
    }

    /**
     * The function takes random weight of {@link BasicNeuron} by and changes it to new number in [-1, 1] interval.
     *
     * @param numOfMutations number of weights that should be changed
     */
    public void mutateRandomWeight(int numOfMutations) {
        for (int i = 0; i < numOfMutations; i++) {
            weights[Util.randomInt(0, weights.length)] = Util.randomDouble();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(String.format("bias: %7.4f  weights:", bias));
        for (double weight : weights) {
            stringBuilder.append(String.format(" %7.4f", weight));
        }
        return stringBuilder.toString();
    }
}
