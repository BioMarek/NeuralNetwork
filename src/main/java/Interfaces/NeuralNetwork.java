package Interfaces;

import Visualizations.DTOs.VisualizationDTO;

public interface NeuralNetwork {
    double[] getNetworkOutput(double[] inputs);

    void printNetwork();

    VisualizationDTO getVisualizationDTO();
}
