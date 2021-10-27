package NeuralNetwork;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

/**
 * The class represents {@link Neuron} which is main building block of {@link Layer}.
 */
public class Neuron {
    public double[] weights;
    public double innerPotential;
    public double bias;

    private Neuron() {
    }

    public Neuron(int numOfNeuronInPrevLayer) {
        weights = Util.randomDoubleArray(numOfNeuronInPrevLayer);
        innerPotential = Util.randomDouble();
        bias = Util.randomDouble();
    }

    /**
     * @return deep copy of {@link Neuron}
     */
    public Neuron copy() {
        Neuron neuron = new Neuron();
        neuron.weights = Arrays.copyOf(this.weights, this.weights.length);
        neuron.innerPotential = this.innerPotential;
        neuron.bias = this.bias;

        return neuron;
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
     * The function takes random weight of {@link Neuron} by and changes it to new number in [-1, 1] interval.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Neuron neuron = (Neuron) o;
        return Double.compare(neuron.innerPotential, innerPotential) == 0 && Double.compare(neuron.bias, bias) == 0 && Arrays.equals(weights, neuron.weights);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(innerPotential, bias);
        result = 31 * result + Arrays.hashCode(weights);
        return result;
    }
}
