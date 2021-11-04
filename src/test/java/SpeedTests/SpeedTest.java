package SpeedTests;

import BasicNeuralNetwork.NeuralNetwork.Neuron;

/**
 * Used to test how fast different implementations of functions are.
 */
public class SpeedTest {
    public static void main(String[] args) {
        Neuron neuron = new Neuron(100);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++){
            neuron.mutateRandomWeight(100);
        }
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) + "ms");
    }
}
