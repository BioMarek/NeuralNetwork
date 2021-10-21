import NeuralNetwork.Layer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LayerTest {
    private static final int TEST_REPEATS = 20;

    private Layer layer;
    private Layer copy;

    @BeforeEach
    void init() {
        layer = new Layer(2, 2, 1);
        copy = layer.copy();
    }

    @Test
    void copy_createsCorrectCopy() {
        assertThat(Arrays.equals(layer.layerOutputs, copy.layerOutputs), is(true));
        assertThat(layer.equals(copy), is(true));
    }

    @Test
    void copy_changeInOutputOfLayerCopyDoesntChangeOriginal() {
        copy.layerOutputs[0] = 10;
        assertThat(layer.layerOutputs[0], not(equalTo(copy.layerOutputs[0])));
        assertThat(layer.equals(copy), is(false));
    }

    @Test
    void copy_changeInNeuronOfLayerCopyDoesntChangeOriginal() {
        copy.neurons[0].weights[0] = 10;
        assertThat(layer.neurons[0].weights[0], not(equalTo(copy.neurons[0].weights[0])));
    }

    @Test
    void copy_changeInLayerOutputsOfLayerCopyDoesntChangeOriginal() {
        copy.layerOutputs[0] = 10;
        assertThat(layer.layerOutputs[0], not(equalTo(copy.layerOutputs[0])));
    }

    @Test
    void getOutput_returnsCorrectOutputForUnitStepFunction() {
        layerTestOutput(1, 1);
        layerTestOutput(1, 0);
        layerTestOutput(0, 1);
        layerTestOutput(0, 0);
    }

    @Test
    void mutateRandomNeuron_changesNeuron() {
        copy.mutateRandomNeuron(1, 1);
        assertThat(layer.neurons, not(equalTo(copy.neurons)));
    }

    private void layerTestOutput(int first, int second) {
        layer.getOutput(new double[]{first, second});
        for (int i = 0; i < TEST_REPEATS; i++) {
            double result1 = (layer.neurons[0].bias +
                            first * layer.neurons[0].weights[0] +
                            second * layer.neurons[0].weights[1]
            ) >= 0.0D ? 1.0D : 0.0D;

            double result2 = (layer.neurons[1].bias +
                            first * layer.neurons[1].weights[0] +
                            second * layer.neurons[1].weights[1]
            ) >= 0.0D ? 1.0D : 0.0D;

            assertThat(layer.layerOutputs[0], equalTo(result1));
            assertThat(layer.layerOutputs[1], equalTo(result2));
        }
    }
}
