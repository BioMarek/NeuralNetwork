package NeuralNetwork;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * The class represents {@link Neuron} which is main building block of {@link Layer}.
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Neuron {
    public double[] weights;
    public double innerPotential;
    public double bias;

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
     * @param prevLayerOutputs The outputs of layer which are used as inputs for layer containing this neuron.
     * @return 1 or 0 based on result of {@link Util#activationFunctionUnitStep(double)}}
     */
    public double getOutput(double[] prevLayerOutputs) {
        double result = bias;
        for (int i = 0; i < weights.length; i++) {
            result += (weights[i] * prevLayerOutputs[i]);
        }
        return Util.activationFunctionUnitStep(result);
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
