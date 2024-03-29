package visualizations.nnVisualization;

import javax.swing.JFrame;

public class NetworkVisualizationFrame extends JFrame {
    public NetworkVisualizationFrame(VisualizationDTO visualizationDTO) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new NeuralNetworkPanel(visualizationDTO));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.pack();
    }
}
