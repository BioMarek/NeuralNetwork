import NeuralNetwork.Neuron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class NeuronTests {
    private static final int TEST_REPEATS = 20;

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

    @Test
    void getOutput_returnsCorrectOutputForUnitStepFunction() {
        neuronTestOutput(1, 1);
        neuronTestOutput(0, 1);
        neuronTestOutput(1, 0);
        neuronTestOutput(0, 0);
    }

    @Test
    void mutateRandomWeight_changesWeight() {
        copy.mutateRandomWeight(1);
        assertThat(Arrays.compare(neuron.weights, copy.weights), not(equalTo(0)));
    }

    private void neuronTestOutput(int first, int second) {
        for (int i = 0; i < TEST_REPEATS; i++) {
            double result = (neuron.bias +
                            first * neuron.weights[0] +
                            second * neuron.weights[1]
            ) >= 0.0D ? 1.0D : 0.0D;
            assertThat(neuron.getOutput(new double[]{first, second}), equalTo(result));
        }
    }
}
