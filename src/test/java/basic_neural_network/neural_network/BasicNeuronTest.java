package basic_neural_network.neural_network;

import utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class BasicNeuronTest {
    private static final int TEST_REPEATS = 20;

    private BasicNeuron basicNeuron;
    private BasicNeuron copy;

    @BeforeEach
    void init() {
        basicNeuron = new BasicNeuron(2);
        copy = basicNeuron.copy();
    }

    @Test
    void copy_createsCorrectCopy() {
        assertThat(basicNeuron.bias, equalTo(copy.bias));
        assertThat(basicNeuron.weights, equalTo(copy.weights));
        assertThat(basicNeuron.equals(copy), is(true));
    }

    @Test
    void copy_changeInCopyBiasDoesntChangeOriginal() {
        copy.bias = 10;
        assertThat(basicNeuron.bias, not(equalTo(copy.bias)));
    }

    @Test
    void copy_changeInCopyWeightDoesntChangeOriginal() {
        copy.weights[0] = 10;
        assertThat(basicNeuron.weights[0], not(equalTo(copy.weights[0])));
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
        assertThat(Arrays.compare(basicNeuron.weights, copy.weights), not(equalTo(0)));
    }

    private void neuronTestOutput(int first, int second) {
        for (int i = 0; i < TEST_REPEATS; i++) {
            double result = (basicNeuron.bias +
                            first * basicNeuron.weights[0] +
                            second * basicNeuron.weights[1]
            ) >= 0.0d ? 1.0d : 0.0d;
            assertThat(basicNeuron.getOutput(new double[]{first, second}, Util.activationFunctionUnitStep()), equalTo(result));
        }
    }
}
