package BasicNeuralNetwork.NeuralNetwork;

import BasicNeuralNetwork.NeuralNetwork.NeuralNetwork;
import Utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NeuralNetworkTest {
    private NeuralNetwork neuralNetwork;
    private NeuralNetwork copy;

    @BeforeEach
    void init() {
        neuralNetwork = new NeuralNetwork(new int[]{1, 1, 1}, Util.activationFunctionUnitStep(), Util.activationFunctionIdentity());
        copy = neuralNetwork.copy();
    }

    @Test
    void neuralNetwork_throwsErrorWhenWrongNumberOfHiddenLayersIsSupplied() {
        assertThrows(IllegalArgumentException.class, () -> neuralNetwork = new NeuralNetwork(
                new int[]{},
                Util.activationFunctionUnitStep(),
                Util.activationFunctionIdentity()));
    }

    @Test
    void copy_createsCorrectCopy() {
        assertThat(neuralNetwork.equals(copy), is(true));
    }

    @Test
    void copy_changeInCopyDoesntChangeOriginal() {
        copy.hiddenLayers.get(0).neurons[0].weights[0] = 10;
        assertThat(copy.hiddenLayers.get(0).neurons[0].weights[0], is(not(neuralNetwork.hiddenLayers.get(0).neurons[0].weights[0])));
        assertThat(neuralNetwork.equals(copy), is(false));
    }

    @Test
    void neuralNetwork_mutateLayersChangesAllLayers() {
        copy.mutateLayers(1, 1);
        assertThat(copy.hiddenLayers.get(0).equals(neuralNetwork.hiddenLayers.get(0)), is(false));
        assertThat(copy.hiddenLayers.get(1).equals(neuralNetwork.hiddenLayers.get(1)), is(false));
    }
}
