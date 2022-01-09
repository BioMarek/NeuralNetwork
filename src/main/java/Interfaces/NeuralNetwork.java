package Interfaces;

import Visualizations.DTOs.VisualizationDTO;
import Visualizations.NeuralNetworkPanel;

public interface NeuralNetwork {
    double[] getNetworkOutput(double[] inputs);

    /**
     * Creates {@link VisualizationDTO} from {@link NeuralNetwork}. It passed to {@link NeuralNetworkPanel} as object
     * from which visualization can be produced.
     *
     * @return {@link VisualizationDTO} holding needed values
     */
    VisualizationDTO getVisualizationDTO();
}
