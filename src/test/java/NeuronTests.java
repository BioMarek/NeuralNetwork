import NeuralNetwork.Neuron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class NeuronTests {
    private Neuron neuron;
    private Neuron copy;

    @BeforeEach
    void init() {
        neuron = new Neuron(2);
        copy = neuron.copy();
    }

    @Test
    void copy_createsCorrectCopy() {
        assertThat(neuron.bias, equalTo(copy.bias));
        assertThat(neuron.output, equalTo(copy.output));
        assertThat(neuron.innerPotential, equalTo(copy.innerPotential));
        assertThat(neuron.weights, equalTo(copy.weights));
        assertThat(neuron.equals(copy), is(true));
    }

    @Test
    void copy_changeOfCopyBiasDoesntChangeOriginal() {
        copy.bias = 10;
        assertThat(neuron.bias, not(equalTo(copy.bias)));
    }

    @Test
    void copy_changeOfCopyWeightDoesntChangeOriginal() {
        copy.weights[0] = 10;
        assertThat(neuron.weights[0], not(equalTo(copy.weights[0])));
    }
}
