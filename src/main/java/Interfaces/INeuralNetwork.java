package Interfaces;

// TODO rename NeuralNetwork and this interface
public interface INeuralNetwork {
    double[] getNetworkOutput(double[] inputs);

    void increaseScore(int amount);
}
