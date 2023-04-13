package visualizations;

import games.snake.SnakeGame;
import neat.evolution.GenePool;
import neat.evolution.Genotype;
import utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NetworkVisualizationFrameTest {
    protected GenePool genePool;
    protected Genotype genotype;
    protected NeuralNetworkPanel neuralNetworkPanel;

    @BeforeEach
    void init() {
        genePool = new GenePool(2, 2, Util.activationFunctionIdentity(), new SnakeGame());
        genotype = genePool.getSpecies().get(0).genotypes.get(0);
        neuralNetworkPanel = new NeuralNetworkPanel(genotype.createPhenotype().getVisualizationDTO());
    }

    @Test
    void weightToColor_returnsCorrectColor() {
        assertThat(neuralNetworkPanel.weightToColor(-1.0D).equals(new Color(255, 0, 0)), is(true));
        assertThat(neuralNetworkPanel.weightToColor(1.0D).equals(new Color(0, 0, 255)), is(true));
    }

//    @Test
//    void layersYAxisOffset_returnsCorrectOffsets() {
//        // TODO make parametrized test
//        BasicNeuralNetwork neuralNetwork = new BasicNeuralNetwork(new int[]{2, 5, 3}, Util.activationFunctionUnitStep(), Util.activationFunctionIdentity());
//        int[] offsets = neuralNetworkPanel.layersYAxisOffset(neuralNetwork);
//        assertThat(offsets[0], is(0));
//        assertThat(offsets[1], is(60));
//
//        BasicNeuralNetwork neuralNetwork2 = new BasicNeuralNetwork(new int[]{2, 4, 1, 2}, Util.activationFunctionUnitStep(), Util.activationFunctionIdentity());
//        int[] offsets2 = neuralNetworkPanel.layersYAxisOffset(neuralNetwork2);
//        assertThat(offsets2[0], is(0));
//        assertThat(offsets2[1], is(90));
//        assertThat(offsets2[2], is(60));
//
//        BasicNeuralNetwork neuralNetwork3 = new BasicNeuralNetwork(new int[]{2, 2, 8, 7}, Util.activationFunctionUnitStep(), Util.activationFunctionIdentity());
//        int[] offsets3 = neuralNetworkPanel.layersYAxisOffset(neuralNetwork3);
//        assertThat(offsets3[0], is(180));
//        assertThat(offsets3[1], is(0));
//        assertThat(offsets3[2], is(30));
//    }
}
