package basic_neural_network.neural_network;

import utils.Util;
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
        copy.basicNeurons[0].weights[0] = 10;
        assertThat(layer.basicNeurons[0].weights[0], not(equalTo(copy.basicNeurons[0].weights[0])));
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
        assertThat(layer.basicNeurons, not(equalTo(copy.basicNeurons)));
    }

    private void layerTestOutput(int first, int second) {
        layer.calculateOutput(new double[]{first, second}, Util.activationFunctionUnitStep());
        for (int i = 0; i < TEST_REPEATS; i++) {
            double result1 = (layer.basicNeurons[0].bias +
                    first * layer.basicNeurons[0].weights[0] +
                    second * layer.basicNeurons[0].weights[1]
            ) >= 0.0d ? 1.0d : 0.0d;

            double result2 = (layer.basicNeurons[1].bias +
                    first * layer.basicNeurons[1].weights[0] +
                    second * layer.basicNeurons[1].weights[1]
            ) >= 0.0d ? 1.0d : 0.0d;

            assertThat(layer.layerOutputs[0], equalTo(result1));
            assertThat(layer.layerOutputs[1], equalTo(result2));
        }
    }
}
