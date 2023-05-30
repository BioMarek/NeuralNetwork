package visualizations.snakeGraphic.explanations;

import games.snake.SnakeMap;
import games.snake.savegame.SavedGameDTO;
import utils.Colors;
import utils.Settings;
import visualizations.snakeGraphic.GridVisualization;
import visualizations.snakeGraphic.SnakeLegend;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import static utils.Util.arrayCopy;

public class SnakeIntroduction implements GridVisualization {
    private Graphics2D graphics;
    private final SnakeLegend snakeLegend;
    private final int columns;
    private final int rows;
    protected int[][] grid;
    private int currentFrame = 0;
    private int totalFrames = 50;
    private final int squareSizePixels = 40;
    private final SavedGameDTO savedGameDTO;

    private int GRID_FRAMES = 50;

    public SnakeIntroduction() {
        this.rows = Settings.GRID_ROWS / Settings.PIXELS_PER_SQUARE;
        this.columns = Settings.GRID_COLUMNS / Settings.PIXELS_PER_SQUARE;
        this.grid = new int[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (Settings.HAS_WALL && (row == 0 || row == rows - 1 || column == 0 || column == columns - 1))
                    grid[row][column] = SnakeMap.WALL.value;
            }
        }

        initialScene();
        savedGameDTO = buildMockDTO();
        this.snakeLegend = new SnakeLegend(graphics, savedGameDTO);
    }

    @Override
    public void createNextFrame() {
        System.out.println("creating frame " + currentFrame);
        currentFrame++;
    }

    @Override
    public void drawPresentation(Graphics2D graphics) {
        this.graphics = graphics;
        snakeLegend.graphics = graphics;
        this.snakeLegend.drawLegend(0);
        drawGrid();

        if (currentFrame > 30) {
            drawSightRays(30, 60);
        }
    }

    @Override
    public boolean stopped() {
        return currentFrame >= totalFrames;
    }

    @Override
    public String name() {
        return "NeuralNetworkIntroduction";
    }

    public void drawGrid() {
        int[][] currentGrid = savedGameDTO.grid.get(currentFrame);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (currentGrid[row][column] == 0)
                    continue;
                numberToColor(currentGrid[row][column]);
                if (currentGrid[row][column] == SnakeMap.FOOD.value) {
                    graphics.fillOval(column * squareSizePixels, row * squareSizePixels, squareSizePixels, squareSizePixels);
                } else {
                    graphics.fillRect(column * squareSizePixels, row * squareSizePixels, squareSizePixels, squareSizePixels);
                }
            }
        }
    }

    public void numberToColor(int num) {
        if (num == -1)
            Colors.setColor(graphics, Colors.WALL.getColor());
        else if (num == SnakeMap.FOOD.value)
            Colors.setColor(graphics, Colors.FOOD.getColor());
        else if (num >= SnakeMap.HEAD.value)
            Colors.setColor(graphics, num - SnakeMap.HEAD.value, 255);
        else if (num >= SnakeMap.BODY.value)
            Colors.setColor(graphics, num - SnakeMap.BODY.value, 150);
    }

    public void initialScene() {
        grid[4][4] = SnakeMap.FOOD.value;
        grid[6][6] = SnakeMap.BODY.value;
        grid[6][5] = SnakeMap.BODY.value;
        grid[6][4] = SnakeMap.HEAD.value;
    }

    public SavedGameDTO buildMockDTO() {
        var saveGame = new SavedGameDTO();
        for (int i = 0; i <= GRID_FRAMES; i++) {
            saveGame.grid.add(arrayCopy(grid));
            saveGame.scores.add(new int[]{0});
        }
        return saveGame;
    }

    public void drawSightRays(int startAppearingFrame, int startDisappearingFrame) {
        var alpha = calculateAppearingAlpha(startAppearingFrame);
        graphics.setColor(Colors.textWithAlpha(alpha));
        graphics.setStroke(new BasicStroke(3f));
        graphics.drawLine(160, 260, 40, 260); // left
        graphics.drawLine(200, 260, 320, 260); // right
        graphics.drawLine(180, 240, 180, 200); // top
        graphics.drawLine(180, 280, 180, 400); // bottom

        graphics.drawLine(160, 240, 40, 120); // top left
        graphics.drawLine(200, 240, 320, 120);// top right
        graphics.drawLine(160, 280, 40, 400); // bottom left
        graphics.drawLine(200, 280, 320, 400);// bottom right
    }

    public int calculateAppearingAlpha(int startAppearingFrame) {
        if (startAppearingFrame + 15 < currentFrame)
            return 255;
        else
            return (int) ((currentFrame - startAppearingFrame) / 15.0 * 255);
    }
}
