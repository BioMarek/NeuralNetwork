package visualizations;

import games.snake.SnakeGameMultiplayer;
import games.snake.dtos.SavedGameDTO;
import interfaces.GridVisualization;
import utils.Settings;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SnakePanel extends JPanel implements ActionListener {
    private GridVisualization gridVisualization;
    private Timer timer;
    private SavedGameDTO savedGameDTO;

    public SnakePanel(SavedGameDTO savedGameDTO) {
        this.setPreferredSize(new Dimension(Settings.backgroundWidth, Settings.backgroundHeight));
        this.setFocusable(true);
        this.savedGameDTO = savedGameDTO;
        gridVisualization = new SnakeVisualization(savedGameDTO);

        startTimer();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        gridVisualization.drawPresentation(graphics);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gridVisualization.createNextFrame();
        repaint();
    }

    /**
     * Starts timer, delay says how often {@link Graphics2D} is repainted.
     */
    public void startTimer() {
        timer = new Timer(Settings.timerDelay, this);
        timer.start();
    }
}
