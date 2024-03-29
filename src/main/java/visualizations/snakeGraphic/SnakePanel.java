package visualizations.snakeGraphic;

import games.snake.savegame.SavedGameDTO;
import utils.Settings;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SnakePanel extends JPanel implements ActionListener {
    private final GridVisualization gridVisualization;

    public SnakePanel(SavedGameDTO savedGameDTO) {
        this.setPreferredSize(new Dimension(Settings.GRID_COLUMN_PIXELS, Settings.GRID_ROW_PIXELS));
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
        Timer timer = new Timer(Settings.TIMER_DELAY, this);
        timer.start();
    }
}
