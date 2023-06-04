package visualizations.snakeGraphic.explanations;

import games.snake.SnakeMap;
import games.snake.savegame.SavedGameDTO;
import utils.Colors;
import utils.Pair;
import utils.Settings;
import visualizations.snakeGraphic.GridVisualization;
import visualizations.snakeGraphic.SnakeLegend;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import static utils.Util.arrayCopy;

public class SnakeIntroduction implements GridVisualization {
    private Graphics2D graphics;
    private final SnakeLegend snakeLegend;
    private final int columns;
    private final int rows;
    protected int[][] grid;
    private int slowFrame = 0;
    private int fastFrame = 0;
    private int totalFrames = 120;
    private final int squareSizePixels = 40;
    private final SavedGameDTO savedGameDTO;
    private int GRID_FRAMES = 120;
    private int GRID_DISAPPEAR = 100;
    private int FONT_SIZE = (int) (Settings.BACKGROUND_HEIGHT / 60 * 1.2);

    public SnakeIntroduction() {
        Settings.VIDEO_FPS = 60;
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
        System.out.println("creating fast frame " + fastFrame);
        fastFrame++;
        if (fastFrame % 3 == 0)
            slowFrame++;
    }

    @Override
    public void drawPresentation(Graphics2D graphics) {
        this.graphics = graphics;
        snakeLegend.graphics = graphics;
        this.snakeLegend.drawLegend(0);
        drawGrid();

        var raysStart = 0; // 30
        if (slowFrame > raysStart) {
            drawSightRays(raysStart, 60);
        }

        var networkStart = 0; // 60
        if (slowFrame > networkStart) {
            drawNetwork(500, 100, networkStart);
        }

        drawMovingNumber("0.5", 180, 120, 505, 128, 60, 20); // top
        drawMovingNumber("0.0", 320, 120, 505, 178, 90, 20); // top right
        drawMovingNumber("0.0", 320, 260, 505, 228, 120, 20); // right
        drawMovingNumber("0.0", 320, 400, 505, 278, 150, 20); // bottom right
        drawMovingNumber("0.0", 180, 400, 505, 328, 180, 20); // bottom
        drawMovingNumber("-0.1", 40, 400, 505, 378, 210, 20); // bottom left
    }

    @Override
    public boolean stopped() {
        return slowFrame >= totalFrames;
    }

    @Override
    public String name() {
        return "NeuralNetworkIntroduction";
    }

    public void drawGrid() {
        int[][] currentGrid = savedGameDTO.grid.get(slowFrame);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (currentGrid[row][column] == 0)
                    continue;
                numberToColor(currentGrid[row][column], calculateDisappearingAlpha(GRID_DISAPPEAR));
                if (currentGrid[row][column] == SnakeMap.FOOD.value) {
                    graphics.fillOval(column * squareSizePixels, row * squareSizePixels, squareSizePixels, squareSizePixels);
                } else {
                    graphics.fillRect(column * squareSizePixels, row * squareSizePixels, squareSizePixels, squareSizePixels);
                }
            }
        }
    }

    public void numberToColor(int num, int alpha) {
        if (num == -1)
            Colors.setColor(graphics, Colors.wallWithAlpha(alpha));
        else if (num == SnakeMap.FOOD.value)
            Colors.setColor(graphics, Colors.foodWithAlpha(alpha));
        else if (num >= SnakeMap.HEAD.value)
            Colors.setColor(graphics, num - SnakeMap.HEAD.value, alpha);
        else if (num >= SnakeMap.BODY.value)
            Colors.setColor(graphics, num - SnakeMap.BODY.value, 150 * alpha / 255);
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
        graphics.drawLine(180, 240, 180, 120); // top
        graphics.drawLine(180, 280, 180, 400); // bottom

        graphics.drawLine(160, 240, 40, 120); // top left
        graphics.drawLine(200, 240, 320, 120);// top right
        graphics.drawLine(160, 280, 40, 400); // bottom left
        graphics.drawLine(200, 280, 320, 400);// bottom right
    }

    public void drawNetwork(int startX, int startY, int startAppearingFrame) {
        drawLayer(startX, startY, 8, 40, 50, startAppearingFrame);
        drawLayer(startX + 200, startY + 100, 4, 40, 50, startAppearingFrame);
        drawWeights(startX, startY, 40, 50, 8, 4);
    }

    public void drawLayer(int startX, int startY, int nodes, int nodeSize, int nodeGap, int startAppearingFrame) {
        var alpha = calculateAppearingAlpha(startAppearingFrame);
        graphics.setColor(Colors.textWithAlpha(alpha));
        graphics.setStroke(new BasicStroke(3f));
        for (int i = 0; i < nodes; i++) {
            graphics.drawOval(startX, startY + i * nodeGap, nodeSize, nodeSize);
        }
    }

    public void drawWeights(int startX, int startY, int nodeSize, int nodeGap, int leftNodes, int rightNodes) {
        List<Integer> leftYs = new ArrayList<>();
        List<Integer> rightYs = new ArrayList<>();
        int leftX = startX + nodeSize;
        int rightX = startX + 200;
        int rightYShift = nodeSize / 2 + (leftNodes - rightNodes) / 2 * (nodeGap + nodeSize);

        for (int l = 0; l < leftNodes; l++) {
            leftYs.add(startY + nodeSize / 2 + l * nodeGap);
        }
        for (int r = 0; r < rightNodes; r++) {
            rightYs.add(rightYShift + nodeSize / 2 + r * nodeGap);
        }


        for (int l = 0; l < leftNodes; l++) {
            for (int r = 0; r < rightNodes; r++) {
                graphics.drawLine(leftX, leftYs.get(l), rightX, rightYs.get(r));
            }
        }
    }

    public void drawMovingNumber(String number, float startX, float startY, float endX, float endY, int startAnimationFastFrame, int startWaitTime) {
        if (fastFrame < startAnimationFastFrame)
            return;

        List<Pair<Float>> coordinates = new ArrayList<>();
        int fastFramesPerMove = 60;
        float axisXShift = (endX - startX) / fastFramesPerMove;
        float axisYShift = (endY - startY) / fastFramesPerMove;

        for (int i = 0; i < fastFramesPerMove; i++) {
            coordinates.add(new Pair<>(startX + axisXShift * i, startY + axisYShift * i));
        }
        graphics.setColor(Colors.TEXT.getColor());
        graphics.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));

        if (startAnimationFastFrame + startWaitTime > fastFrame) {
            graphics.drawString(number, startX, startY);
        } else if (startAnimationFastFrame + fastFramesPerMove + startWaitTime > fastFrame) {
            var currentCoordinate = coordinates.get(fastFrame - startAnimationFastFrame - startWaitTime);
            graphics.drawString(number, currentCoordinate.getFirst(), currentCoordinate.getSecond());
        } else {
            graphics.drawString(number, endX, endY);
        }
    }

    public int calculateAppearingAlpha(int startAppearingFrame) {
        if (startAppearingFrame + Settings.FADEIN_FRAMES < slowFrame)
            return 255;
        else
            return (int) ((slowFrame - startAppearingFrame) / 15.0 * 255);
    }

    public int calculateDisappearingAlpha(int startDisappearingFrame) {
        if (startDisappearingFrame >= slowFrame)
            return 255;
        if (startDisappearingFrame + Settings.FADEIN_FRAMES < slowFrame)
            return 0;
        else
            return 255 - (int) ((slowFrame - startDisappearingFrame) / 15.0 * 255);
    }
}
