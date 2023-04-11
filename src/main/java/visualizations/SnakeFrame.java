package visualizations;

import javax.swing.JFrame;

public class SnakeFrame extends JFrame {
    public SnakeFrame(SnakePanel snakePanel) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(snakePanel);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.pack();
    }
}