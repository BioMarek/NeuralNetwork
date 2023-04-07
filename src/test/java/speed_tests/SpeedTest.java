package speed_tests;

import basic_neural_network.neural_network.BasicNeuron;

/**
 * Used to test how fast different implementations of functions are.
 */
public class SpeedTest {
    public static void main(String[] args) {
        BasicNeuron basicNeuron = new BasicNeuron(100);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++){
            basicNeuron.mutateRandomWeight(100);
        }
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) + "ms");
    }
}
