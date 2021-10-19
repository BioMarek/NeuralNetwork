package Visualization;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        JPanel panel = new NeuralNetworkPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.pack();
    }
}
