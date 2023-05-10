package TestUtils;

import neat.phenotype.NeuralNetwork;
import visualizations.nnVisualization.VisualizationDTO;

/**
 * Implementation of {@link NeuralNetwork} for testing purposes that returns predetermined values.
 */
public class TestNeuralNetwork implements NeuralNetwork {
    /**
     * Provides on of the test outputs. Only for testing purposes.
     *
     * @param inputs is ignored
     * @return output that looks like real one only always same
     */
    @Override
    public double[] getNetworkOutput(double[] inputs) {
        return new double[]{1.0, 0.5, 0.5, 0.5};
    }

    @Override
    public VisualizationDTO getVisualizationDTO() {
        return null;
    }
}
