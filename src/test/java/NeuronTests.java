import NeuralNetwork.Neuron;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class NeuronTests {
    Neuron neuron = new Neuron(2);
    Neuron copy = neuron.copy();

    @Test
    void copy() {
        assertThat(neuron.bias, is(copy.bias));
        assertThat(neuron.output, is(copy.output));
        assertThat(neuron.innerPotential, is(copy.innerPotential));
        assertThat(neuron.weights, is(copy.weights));
        assertThat(neuron.equals(copy), is(true));
    }
}
