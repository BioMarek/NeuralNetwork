package NeuralNetwork;

import java.util.Arrays;
import java.util.Objects;

public class Neuron {
    public double[] weights;
    public double innerPotential;
    public double output;
    public double bias;

    private Neuron(){
    }

    public Neuron(int numOfNeuronInPrevLayer) {
        weights = Util.randomDoubleArray(numOfNeuronInPrevLayer);
        innerPotential = Util.randomDouble();
        output = Util.randomDouble();
        bias = Util.randomDouble();
    }

    public Neuron copy(){
        Neuron neuron = new Neuron();
        neuron.weights = Arrays.copyOf(this.weights, this.weights.length);
        neuron.innerPotential = this.innerPotential;
        neuron.output = this.output;
        neuron.bias = this.bias;

        return neuron;
    }

    public double getOutput(double[] prevLayerOutputs){
        double result = bias;
        for (int i = 0; i < weights.length; i++){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Neuron neuron = (Neuron) o;
        return Double.compare(neuron.innerPotential, innerPotential) == 0
                && Double.compare(neuron.output, output) == 0
                && Double.compare(neuron.bias, bias) == 0 &&
                Arrays.equals(weights, neuron.weights);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(innerPotential, output, bias);
        result = 31 * result + Arrays.hashCode(weights);
        return result;
    }
}
