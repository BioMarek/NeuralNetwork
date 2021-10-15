import NeuralNetwork.Layer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LayerTest {
    private Layer layer;
    private Layer copy;

    @BeforeEach
    void init() {
        layer = new Layer(2, 2);
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
        layer.getOutput(new double[]{1, 1});
        double result1 = (layer.neurons[0].bias + layer.neurons[0].weights[0] + layer.neurons[0].weights[1]) >= 0.0D ? 1.0D : 0.0D;
        double result2 = (layer.neurons[1].bias + layer.neurons[1].weights[0] + layer.neurons[1].weights[1]) >= 0.0D ? 1.0D : 0.0D;

        assertThat(layer.layerOutputs[0], equalTo(result1));
        assertThat(layer.layerOutputs[1], equalTo(result2));

        layer.getOutput(new double[]{0, 1});
        result1 = (layer.neurons[0].bias + layer.neurons[0].weights[1]) >= 0.0D ? 1.0D : 0.0D;
        result2 = (layer.neurons[1].bias + layer.neurons[1].weights[1]) >= 0.0D ? 1.0D : 0.0D;

        assertThat(layer.layerOutputs[0], equalTo(result1));
        assertThat(layer.layerOutputs[1], equalTo(result2));

        layer.getOutput(new double[]{1, 0});
        result1 = (layer.neurons[0].bias + layer.neurons[0].weights[0]) >= 0.0D ? 1.0D : 0.0D;
        result2 = (layer.neurons[1].bias + layer.neurons[1].weights[0]) >= 0.0D ? 1.0D : 0.0D;

        assertThat(layer.layerOutputs[0], equalTo(result1));
        assertThat(layer.layerOutputs[1], equalTo(result2));
    }
}
