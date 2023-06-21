package visualizations.snakeGraphic.explanations;

import games.snake.SnakeMap;
import games.snake.savegame.SavedGameDTO;
import utils.Colors;
import utils.Pair;
import utils.Settings;
import utils.Util;
import visualizations.snakeGraphic.GridVisualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


public class SnakeIntroduction implements GridVisualization {
    private NetworkGraph networkGraph;
    private Graphics2D graphics;
    private final int columns;
    private final int rows;
    private final int SQUARE_PIXEL_SIZE = 40;
    private final SavedGameDTO savedGameDTO;
    private final int FONT_SIZE = (int) (Settings.BACKGROUND_HEIGHT / 60 * 1.2);
    private final int[][] grid;
    private int slowFrame = 0;
    private int fastFrame = 0;
    private final int totalFrames = 360; // 480
    private final int gridFrames = 300;
    private final int gridDisappear = 270;
    private float networkScale = 1.0f;
    private int networkStartX = 750;
    private int networkStartY = 100;

    public SnakeIntroduction() {
        Settings.VIDEO_FPS = 60;
        this.networkGraph = new NetworkGraph(260, 605, 62);
        this.rows = Settings.GRID_ROW_PIXELS / SQUARE_PIXEL_SIZE;
        this.columns = Settings.GRID_COLUMN_PIXELS / SQUARE_PIXEL_SIZE;
        this.grid = new int[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (Settings.HAS_WALL && (row == 0 || row == rows - 1 || column == 0 || column == columns - 1))
                    grid[row][column] = SnakeMap.WALL.value;
            }
        }

        initialScene();
        savedGameDTO = buildMockDTO();
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
        networkGraph.graphics = graphics;
        networkGraph.slowFrame = slowFrame;
        networkGraph.fastFrame = fastFrame;
//        if (slowFrame < gridFrames)
//            drawGrid();
//
//        var raysStart = 30;
//        if (slowFrame > raysStart) {
//            drawSightRays(raysStart, 270);
//        }
//
//        var networkStart = 90;
//        var startShrinking = 420;
//        drawShrinkingNetwork(networkStart, startShrinking, 300);
//
//        var outputDirection = 150;
//        if (slowFrame > outputDirection && slowFrame <= startShrinking) {
//            drawText("UP", 1120, 317, Colors.TEXT.getColor(), Color.RED, 360); // up
//            drawText("LEFT", 1120, 397); // right
//            drawText("DOWN", 1120, 477); // down
//            drawText("RIGHT", 1120, 557); // left
//        }
//
//        var startNumberMoveFastFrame = 510;
//        if (slowFrame <= startShrinking) {
//            drawMovingNumber("0.5", 180, 120, 760, 132, startNumberMoveFastFrame, 20); // top
//            drawMovingNumber("0.0", 320, 120, 760, 212, startNumberMoveFastFrame + 30, 20); // top right
//            drawMovingNumber("0.0", 320, 260, 760, 292, startNumberMoveFastFrame + 60, 20); // right
//            drawMovingNumber("0.0", 320, 400, 760, 372, startNumberMoveFastFrame + 90, 20); // bottom right
//            drawMovingNumber("0.0", 180, 400, 760, 452, startNumberMoveFastFrame + 120, 20); // bottom
//            drawMovingNumber("-0.1", 40, 400, 755, 532, startNumberMoveFastFrame + 150, 20); // bottom left
//            drawMovingNumber("-0.1", 40, 260, 755, 612, startNumberMoveFastFrame + 180, 20); // left
//            drawMovingNumber("-0.1", 40, 120, 755, 692, startNumberMoveFastFrame + 210, 20); // left
//        }
//
//        var resultNumbersStart = 330;
//        if (slowFrame > resultNumbersStart && slowFrame <= startShrinking) {
//            drawText("0.9", 1060, 317, Colors.TEXT.getColor(), Color.RED, 360); // up
//            drawText("0.5", 1060, 397); // right
//            drawText("-0.1", 1055, 477); // down
//            drawText("-0.3", 1055, 557); // left
//        }

        var networkStart = 0;
        var startShrinking = 30;
        drawShrinkingNetwork(networkStart, startShrinking, 30);

        //TODO move to networkGraph single function
        networkGraph.drawGraphAxis();
        networkGraph.drawGraphBars(210);
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
                numberToColor(currentGrid[row][column], calculateDisappearingAlpha(gridDisappear));
                if (currentGrid[row][column] == SnakeMap.FOOD.value) {
                    graphics.fillOval(column * SQUARE_PIXEL_SIZE, row * SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE);
                } else {
                    graphics.fillRect(column * SQUARE_PIXEL_SIZE, row * SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE);
                }
            }
        }
    }

    public void numberToColor(int num, int alpha) {
        if (num == -1)
            Colors.setColor(graphics, Colors.wallWithAlpha(alpha));
        else if (num == SnakeMap.FOOD.value)
            Colors.setColor(graphics, Colors.redWithAlpha(alpha));
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
        for (int i = 0; i <= gridFrames; i++) {
            saveGame.grid.add(Util.arrayCopy(grid));
            saveGame.scores.add(new int[]{0});
        }
        return saveGame;
    }

    public void drawSightRays(int startAppearingFrame, int startDisappearingFrame) {
        var appearingAlpha = calculateAppearingAlpha(startAppearingFrame);
        var disapperatingAlpha = calculateDisappearingAlpha(startDisappearingFrame);
        var alpha = 0;
        if (slowFrame >= startAppearingFrame && slowFrame < startDisappearingFrame)
            alpha = appearingAlpha;
        else if (slowFrame >= startDisappearingFrame) {
            alpha = disapperatingAlpha;
        }

        graphics.setColor(Colors.textWithAlpha(alpha));
        graphics.setStroke(new BasicStroke(3f));
        graphics.drawLine(160, 260, 40, 260); // left
        graphics.drawLine(200, 260, 320, 260); // right
        graphics.drawLine(180, 240, 180, 120); // top
        graphics.drawLine(180, 280, 180, 400); // bottom

        graphics.drawLine(160, 240, 40, 120); // top left
        graphics.drawLine(200, 240, 320, 120); // top right
        graphics.drawLine(160, 280, 40, 400); // bottom left
        graphics.drawLine(200, 280, 320, 400); // bottom right
    }

    public void drawShrinkingNetwork(int startAppearingFrame, int startShrinking, int startMovingDots) {
        if (slowFrame > startAppearingFrame) {
            if (slowFrame > startShrinking) {
                networkScale -= 0.01f;
                networkStartX -= 5;
                networkStartY += 5;
            }
            if (networkScale > 0.02)
                drawNetwork(networkStartX, networkStartY, startAppearingFrame, startMovingDots);
        }
    }

    public void drawNetwork(int startX, int startY, int startAppearingFrame, int startMovingDots) {
        int nodeSize = (int) (50 * networkScale);
        int nodeGap = (int) (80 * networkScale);
        drawLayer(startX, startY, 8, nodeSize, nodeGap, startAppearingFrame);
        drawLayer(startX + nodeSize * 6, startY + nodeGap * 2 + nodeSize / 2, 4, nodeSize, nodeGap, startAppearingFrame);
        drawWeights(startX, startY, nodeSize, nodeGap, 8, 4, startMovingDots);
    }

    public void drawLayer(int startX, int startY, int nodes, int nodeSize, int nodeGap, int startAppearingFrame) {
        var alpha = calculateAppearingAlpha(startAppearingFrame);
        graphics.setColor(Colors.textWithAlpha(alpha));
        graphics.setStroke(new BasicStroke(3f));
        for (int i = 0; i < nodes; i++) {
            graphics.drawOval(startX, startY + i * nodeGap, nodeSize, nodeSize);
        }
    }

    public void drawWeights(int startX, int startY, int nodeSize, int nodeGap, int leftNodes, int rightNodes, int startMovingDot) {
        List<Integer> leftYs = new ArrayList<>();
        List<Integer> rightYs = new ArrayList<>();
        int leftX = startX + nodeSize;
        int rightX = startX + nodeSize * 6;
        int rightYShift = startY + nodeGap * 2 + nodeSize / 2;

        for (int l = 0; l < leftNodes; l++) {
            leftYs.add(startY + nodeSize / 2 + l * nodeGap);
        }
        for (int r = 0; r < rightNodes; r++) {
            rightYs.add(rightYShift + nodeSize / 2 + r * nodeGap);
        }

        for (int l = 0; l < leftNodes; l++) {
            for (int r = 0; r < rightNodes; r++) {
                graphics.drawLine(leftX, leftYs.get(l), rightX, rightYs.get(r));
                if ((slowFrame >= startMovingDot && slowFrame <= startMovingDot + Settings.CHANGING_SLOW_FRAMES * 2)
                        && (l == 0 || l == 5 || l == 6 || l == 7)) { // shows dots moving just from non-zero inputs
                    int dotSize = 10;
                    int stage = slowFrame - startMovingDot;
                    var pair = calculateMovingDotCoordinates(leftX, leftYs.get(l), rightX, rightYs.get(r), stage);
                    graphics.fillOval(pair.getFirst() - dotSize / 2, pair.getSecond() - dotSize / 2, dotSize, dotSize);
                }
            }
        }
    }

    public Pair<Integer> calculateMovingDotCoordinates(int startX, int startY, int endX, int endY, int stage) {
        int totalFrames = Settings.CHANGING_SLOW_FRAMES * 2;
        if (stage <= 0) {
            return new Pair<>(startX, startY);
        } else if (stage >= totalFrames) {
            return new Pair<>(endX, endY);
        } else {
            int x = startX + (endX - startX) * stage / totalFrames;
            int y = startY + (endY - startY) * stage / totalFrames;
            return new Pair<>(x, y);
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

    public void drawText(String text, float startX, float startY) {
        graphics.setColor(Colors.TEXT.getColor());
        graphics.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        graphics.drawString(text, startX, startY);
    }

    public void drawText(String text, float startX, float startY, Color from, Color to, int startChangingFrame) {
        var color = calculateChangingColor(from, to, startChangingFrame);
        graphics.setColor(color);
        graphics.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        graphics.drawString(text, startX, startY);
    }

    public int calculateAppearingAlpha(int startAppearingFrame) {
        if (startAppearingFrame + Settings.CHANGING_SLOW_FRAMES < slowFrame)
            return 255;
        else
            return (int) ((slowFrame - startAppearingFrame) / 15.0 * 255);
    }

    public int calculateDisappearingAlpha(int startDisappearingFrame) {
        if (startDisappearingFrame >= slowFrame)
            return 255;
        if (startDisappearingFrame + Settings.CHANGING_SLOW_FRAMES < slowFrame)
            return 0;
        else
            return 255 - (int) ((slowFrame - startDisappearingFrame) / 15.0 * 255);
    }

    public Color calculateChangingColor(Color from, Color to, int startChangingFrame) {
        int stage;
        if (slowFrame < startChangingFrame)
            stage = 0;
        else if (slowFrame >= startChangingFrame + Settings.CHANGING_SLOW_FRAMES) {
            stage = Settings.CHANGING_SLOW_FRAMES;
        } else
            stage = slowFrame - startChangingFrame;
        return Colors.colorMixing(from, to, stage);
    }
}
