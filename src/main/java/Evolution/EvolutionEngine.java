package Evolution;

import NeuralNetwork.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class EvolutionEngine {
    // TODO needs redesign
    List<NeuralNetwork> neuralNetworks = new ArrayList<NeuralNetwork>();
    double[] inputs;
    int[] outputs;

    public EvolutionEngine(int numOfNetworks, int[] sizes, double[] inputs, int[] outputs) {
        for (int i = 0; i < numOfNetworks; i++) {
            neuralNetworks.add(new NeuralNetwork(sizes));
        }
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public void nextGeneration() {

    }
}
