package Visualization;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MainFrameTest {

    NeuralNetworkPanel neuralNetworkPanel = new NeuralNetworkPanel();

    @Test
    void weightToColor_returnsCorrectColor() {
        assertThat(neuralNetworkPanel.weightToColor(-1.0D).equals(new Color(255, 0, 0)), is(true));
        assertThat(neuralNetworkPanel.weightToColor(1.0D).equals(new Color(0, 0, 255)), is(true));
    }
}
