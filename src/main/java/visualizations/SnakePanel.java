package visualizations;

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
    private GridVisualization gridVisualization = new SnakeVisualization();
    private Timer timer;

    public SnakePanel() {
        this.setPreferredSize(new Dimension(Settings.backgroundWidth, Settings.backgroundHeight));
        this.setFocusable(true);

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
