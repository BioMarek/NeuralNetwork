package NeuralNetwork;

import java.util.Arrays;
import java.util.Objects;

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
     * Calculates output of neuron using {@link Util#activationFunctionUnitStep(double)}}.
     *
     * @param prevLayerOutputs the outputs of layer which are used as inputs for layer containing this neuron
     * @return 1 or 0 based on result of {@link Util#activationFunctionUnitStep(double)}}
     */
    public double getOutput(double[] prevLayerOutputs) {
        double result = bias;
        for (int i = 0; i < weights.length; i++) {
            result += (weights[i] * prevLayerOutputs[i]);
        }
        return Util.activationFunctionUnitStep(result);
    }

    /**
     * Calculates output of neuron without any activation function
     *
     * @param prevLayerOutputs the outputs of layer which are used as inputs for layer containing this neuron
     * @return multiple of previous layer outputs and weights
     */
    public double getOutputRaw(double[] prevLayerOutputs) {
        double result = bias;
        for (int i = 0; i < weights.length; i++) {
            result += (weights[i] * prevLayerOutputs[i]);
        }
        return result;
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
