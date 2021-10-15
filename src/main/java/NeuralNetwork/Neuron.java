package NeuralNetwork;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Neuron {
    public double[] weights;
    public double innerPotential;
    public double output;
    public double bias;

    public Neuron(int numOfNeuronInPrevLayer) {
        weights = Util.randomDoubleArray(numOfNeuronInPrevLayer);
        innerPotential = Util.randomDouble();
        output = Util.randomDouble();
        bias = Util.randomDouble();
    }

    public Neuron copy() {
        Neuron neuron = new Neuron();
        neuron.weights = Arrays.copyOf(this.weights, this.weights.length);
        neuron.innerPotential = this.innerPotential;
        neuron.output = this.output;
        neuron.bias = this.bias;

        return neuron;
    }

    public double getOutput(double[] prevLayerOutputs) {
        double result = bias;
        for (int i = 0; i < weights.length; i++) {
            result += (weights[i] * prevLayerOutputs[i]);
        }
        return Util.activationFunctionUnitStep(result);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(String.format("bias: %7.4f  output: %7.4f  weights:", bias, output));
        for (double weight : weights) {
            stringBuilder.append(String.format(" %7.4f", weight));
        }
        return stringBuilder.toString();
    }
}
