package visualizations;

import javax.swing.JFrame;
import java.awt.Toolkit;

public class SnakeFrame extends JFrame {
    public SnakeFrame(SnakePanel snakePanel) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(snakePanel);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setVisible(true);
        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height);
        this.setLocationRelativeTo(null);
    }
}
