package Interfaces;

public interface NeuralNetwork {
    double[] getNetworkOutput(double[] inputs);

    void increaseScore(int amount);

    void printNetwork();
}
