package NeuralNetwork;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Stream;

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

    public Layer copy() {
        Layer layer = new Layer();
        layer.neurons = Arrays.copyOf(this.neurons, this.neurons.length);
        layer.layerOutputs = Arrays.copyOf(this.layerOutputs, this.layerOutputs.length);
        return layer;
    }

    public void getOutput(double[] prevLayerOutputs) {
        for (int i = 0; i < neurons.length; i++) {
            layerOutputs[i] = neurons[i].getOutput(prevLayerOutputs);
        }
    }

    public void printLayer() {
        Stream.of(neurons).forEach(System.out::println);
    }
}
