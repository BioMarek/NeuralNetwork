package NeuralNetwork;

import java.util.stream.Stream;

public class Layer {
    public int numOfNeurons;
    public int numOfNeuronInPrevLayer;
    public Neuron[] neurons;
    public double[] layerOutputs;

    public Layer(int numOfNeuronInPrevLayer, int numOfNeurons) {
        this.numOfNeurons = numOfNeurons;
        this.numOfNeuronInPrevLayer = numOfNeuronInPrevLayer;
        this.neurons = new Neuron[numOfNeurons];
        this.layerOutputs = new double[numOfNeurons];

        for (int i = 0; i < numOfNeurons; i++) {
            neurons[i] = new Neuron(numOfNeuronInPrevLayer);
        }
    }

    public void calculateLayerOutput(double[] prevLayerOutputs) {
        for (int i = 0; i < numOfNeurons; i++) {
            layerOutputs[i] = neurons[i].getOutput(prevLayerOutputs);
        }
    }

    public void printLayer() {
        Stream.of(neurons).forEach(System.out::println);
    }
}
