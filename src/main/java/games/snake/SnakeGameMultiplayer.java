package games.snake;

import games.MultiplayerGame;
import games.snake.dtos.SnakeSightRaysDTO;
import games.snake.dtos.SnakeTopDownDTO;
import games.snake.savegame.SavedGameDTO;
import neat.phenotype.NeuralNetwork;
import utils.Direction;
import utils.Settings;

import java.util.ArrayList;
import java.util.List;

import static utils.Util.arrayCopy;
import static utils.Util.randomFreeCoordinate;
import static utils.Util.repeat;

public class SnakeGameMultiplayer extends AbstractSnakeGame implements MultiplayerGame {
    protected List<Snake> snakes;

    public SnakeGameMultiplayer() {
        this.rows = Settings.GRID_ROW_PIXELS / Settings.PIXELS_PER_SQUARE;
        this.columns = Settings.GRID_COLUMN_PIXELS / Settings.PIXELS_PER_SQUARE;
        reset();
    }

    @Override
    public int[] play(List<NeuralNetwork> neuralNetworks) {
        for (int i = 0; i < snakes.size(); i++)
            snakes.get(i).neuralNetwork = neuralNetworks.get(i);

        for (int move = 0; move < Settings.MAX_NUM_OF_MOVES; move++) {
            for (Snake snake : snakes) {
                var networkOutput = snake.neuralNetwork.getNetworkOutput(snakeSightDTO.getInput(snake));
                moveSnakeToDirection(snake, outputToDirection(networkOutput));
            }
        }

        return snakes.stream()
                .mapToInt(snake -> snake.snakeScore)
                .toArray();
    }

    @Override
    public void reset() {
        initGrid();
        initSnakes();
        switch (Settings.SNAKE_SIGHT_TYPE) {
            case RAYS -> snakeSightDTO = new SnakeSightRaysDTO(grid);
            case TOP_DOWN -> snakeSightDTO = new SnakeTopDownDTO(grid);
        }
        numOfFood = 0;
        repeat.accept(Settings.MAX_NUM_OF_FOOD, this::placeFood);
    }

    private void initSnakes() {
        snakes = new ArrayList<>();
        for (int i = 0; i < Settings.NUM_OF_PLAYERS; i++) {
            var coordinates = randomFreeCoordinate(grid);
            var snake = new Snake(grid, coordinates.getFirst(), coordinates.getSecond(), Direction.NONE, i);
            snake.placeSnake();
            snakes.add(snake);
        }
    }

    /**
     * Moves Games.Snake to given direction
     *
     * @param direction where to move snake
     */
    protected void moveSnakeToDirection(Snake snake, Direction direction) {
        if (Settings.SELF_COLLISION) {
            // If SELF_COLLISION is enabled than snake that is longer than one body part cannot go opposite to its last direction because it would hit its tail
            if (snake.size() > 1 && direction == Direction.opposite(snake.lastDirection) || direction == Direction.NONE)
                direction = snake.lastDirection;
        }
        snake.lastDirection = direction;

        int headRow = snake.bodyParts.get(0).row;
        int headColumn = snake.bodyParts.get(0).column;

        switch (direction) {
            case UP -> moveSnake(snake, headRow - 1, headColumn);
            case DOWN -> moveSnake(snake, headRow + 1, headColumn);
            case LEFT -> moveSnake(snake, headRow, headColumn - 1);
            case RIGHT -> moveSnake(snake, headRow, headColumn + 1);
        }
    }


    /**
     * Moves snake if there is collision with other snake or wall, snake will be reset.
     *
     * @param snake  to move
     * @param row    where to move
     * @param column where to move
     */
    protected void moveSnake(Snake snake, int row, int column) {
        if (!Settings.HAS_WALL) {
            row = wrapAroundCoordinates(row, rows);
            column = wrapAroundCoordinates(column, columns);
        }
        if (snakeCollision(snake, row, column)) {
            var coordinates = randomFreeCoordinate(grid);
            var foodPlaced = snake.removeSnake(Settings.LEAVE_CORPSE);
            numOfFood += foodPlaced;
            snake.resetSnake(coordinates.getFirst(), coordinates.getSecond(), Direction.randomDirection(), 3);
            snake.snakeScore += Settings.DEATH_PENALTY;
            snake.placeSnake();
        } else {
            moveSnakeByOne(snake, row, column);
            if (snake.stepsMoved == Settings.STEPS_TO_REDUCTION) {
                snake.stepsMoved = 0;
                snake.reduceSnakeByOne();
            }
        }
    }

    /**
     * Moves snake to given row and column. If there is food snake length will be increased by one and new food will
     * be placed onto grid.
     *
     * @param snake  to move
     * @param row    where to move
     * @param column where to move
     */
    protected void moveSnakeByOne(Snake snake, int row, int column) {
        var bodyParts = snake.bodyParts;

        bodyParts.get(0).isHead = false;
        bodyParts.add(0, new BodyPart(true, row, column));

        if (grid[row][column] == SnakeMap.FOOD.value) {
            snake.placeSnake();
            numOfFood--;
            placeFood();
            snake.snakeScore += 1;
        } else {
            snake.removeSnake(false);
            bodyParts.remove(bodyParts.size() - 1);
            snake.placeSnake();
        }

        snake.stepsMoved++;
    }

    /**
     * Checks whether snake can move specific grid coordinates.
     *
     * @return true moving to coordinates will result in death
     */
    protected boolean snakeCollision(Snake snake, int row, int column) {
        return grid[row][column] == SnakeMap.WALL.value || snake.isSnakeCollision(row, column);
    }

    /**
     * Plays the game and saves grid arrangements so they can be used later e.g. for visualization.
     */
    @Override
    public SavedGameDTO saveSnakeMoves(List<NeuralNetwork> neuralNetworks) {
        var savedGameDTO = new SavedGameDTO();
        int frameCount = 0;
        for (int move = 0; move < Settings.MAX_NUM_OF_MOVES_VIDEO; move++) {
            frameCount++;
            for (int i = 0; i < neuralNetworks.size(); i++) {
                var networkOutput = neuralNetworks.get(i).getNetworkOutput(snakeSightDTO.getInput(snakes.get(i)));
                moveSnakeToDirection(snakes.get(i), outputToDirection(networkOutput));
            }
            int[] scores = new int[snakes.size()];
            for (int i = 0; i < neuralNetworks.size(); i++) {
                scores[i] = snakes.get(i).snakeScore;
            }
            savedGameDTO.scores.add(scores);
            savedGameDTO.grid.add(arrayCopy(grid));
        }
        setSaveGameMetadata(savedGameDTO);
        savedGameDTO.totalFrames = frameCount;
        return savedGameDTO;
    }
}
