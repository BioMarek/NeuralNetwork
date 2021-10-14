import NeuralNetwork.NeuralNetwork;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        NeuralNetwork neuralNetwork = new NeuralNetwork(new int[]{5, 5, 5});
        neuralNetwork.printNeuralNetwork();
        double[] input = new double[]{0, 0, 1, 1, 0};
        System.out.println(Arrays.toString(neuralNetwork.getNetworkOutput(input)));
    }
}
