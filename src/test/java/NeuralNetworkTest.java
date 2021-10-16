import NeuralNetwork.NeuralNetwork;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NeuralNetworkTest {
    private NeuralNetwork neuralNetwork;

    @Test
    void NeuralNetwork_throwsErrorWhenWrongNumberOfHiddenLayersIsSupplied() {
        assertThrows(IllegalArgumentException.class, () -> neuralNetwork = new NeuralNetwork(new int[]{}));
    }
}
