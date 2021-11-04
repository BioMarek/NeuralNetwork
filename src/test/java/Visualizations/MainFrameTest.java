package Visualizations;

import BasicNeuralNetwork.NeuralNetwork.NeuralNetwork;
import Utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MainFrameTest {

    NeuralNetworkPanel neuralNetworkPanel;

    @BeforeEach
    void init() {
        neuralNetworkPanel = new NeuralNetworkPanel();
    }

    @Test
    void weightToColor_returnsCorrectColor() {
        assertThat(neuralNetworkPanel.weightToColor(-1.0D).equals(new Color(255, 0, 0)), is(true));
        assertThat(neuralNetworkPanel.weightToColor(1.0D).equals(new Color(0, 0, 255)), is(true));
    }

    @Test
    void layersYAxisOffset_returnsCorrectOffsets() {
        // TODO make parametrized test
        NeuralNetwork neuralNetwork = new NeuralNetwork(new int[]{2, 5, 3}, Util.activationFunctionUnitStep(), Util.activationFunctionIdentity());
        int[] offsets = neuralNetworkPanel.layersYAxisOffset(neuralNetwork);
        assertThat(offsets[0], is(0));
        assertThat(offsets[1], is(60));

        NeuralNetwork neuralNetwork2 = new NeuralNetwork(new int[]{2, 4, 1, 2}, Util.activationFunctionUnitStep(), Util.activationFunctionIdentity());
        int[] offsets2 = neuralNetworkPanel.layersYAxisOffset(neuralNetwork2);
        assertThat(offsets2[0], is(0));
        assertThat(offsets2[1], is(90));
        assertThat(offsets2[2], is(60));

        NeuralNetwork neuralNetwork3 = new NeuralNetwork(new int[]{2, 2, 8, 7}, Util.activationFunctionUnitStep(), Util.activationFunctionIdentity());
        int[] offsets3 = neuralNetworkPanel.layersYAxisOffset(neuralNetwork3);
        assertThat(offsets3[0], is(180));
        assertThat(offsets3[1], is(0));
        assertThat(offsets3[2], is(30));
    }
}
