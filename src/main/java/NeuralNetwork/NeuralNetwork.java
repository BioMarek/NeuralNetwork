package NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private final List<Layer> hiddenLayers;

    public NeuralNetwork(int[] sizes) {
        this.hiddenLayers = new ArrayList<>();

        if (sizes.length > 1) {
            for (int i = 1; i < sizes.length; ++i) {
                this.hiddenLayers.add(new Layer(sizes[i - 1], sizes[i]));
            }
        }
    }

    public double[] getNetworkOutput(double[] inputs) {
        hiddenLayers.get(0).getOutput(inputs);

        for (int i = 1; i < hiddenLayers.size(); i++) {
            hiddenLayers.get(1).getOutput(hiddenLayers.get(0).layerOutputs);
        }

        return hiddenLayers.get(hiddenLayers.size() - 1).layerOutputs;
    }

    public void printNeuralNetwork() {
        for (int i = 0; i < hiddenLayers.size(); i++) {
            System.out.println("Layer: " + i);
            hiddenLayers.get(i).printLayer();
        }
    }
}
