package Visualizations;

import Visualizations.DTOs.VisualizationDTO;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame(VisualizationDTO visualizationDTO) {
        JPanel panel = new NeuralNetworkPanel(visualizationDTO);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.pack();
    }
}
