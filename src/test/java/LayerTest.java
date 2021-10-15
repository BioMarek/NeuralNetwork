import NeuralNetwork.Layer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LayerTest {
    Layer layer = new Layer(2, 2);
    Layer copy = layer.copy();

    @Test
    void copy() {
        assertThat(Arrays.equals(layer.layerOutputs, copy.layerOutputs), is(true));
        assertThat(layer.equals(copy), is(true));
    }
}
