package visualizations;

import games.snake.savegame.SavedGameDTO;
import interfaces.GridVisualization;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static utils.Settings.GRID_COLUMNS;
import static utils.Settings.GRID_ROWS;
import static utils.Settings.TIMER_DELAY;

public class SnakePanel extends JPanel implements ActionListener {
    private final GridVisualization gridVisualization;

    public SnakePanel(SavedGameDTO savedGameDTO) {
        this.setPreferredSize(new Dimension(GRID_COLUMNS, GRID_ROWS));
        this.setFocusable(true);
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
        Timer timer = new Timer(TIMER_DELAY, this);
        timer.start();
    }
}
