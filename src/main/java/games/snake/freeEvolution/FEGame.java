package games.snake.freeEvolution;

import games.snake.AbstractSnakeGame;
import games.snake.BodyPart;
import games.snake.SnakeMap;
import games.snake.dtos.SnakeSightRaysDTO;
import games.snake.dtos.SnakeTopDownDTO;
import games.snake.savegame.SavedGameDTO;
import utils.Direction;
import utils.Settings;

import java.util.ArrayList;
import java.util.List;

import static utils.Util.arrayCopy;
import static utils.Util.randomFreeCoordinate;
import static utils.Util.repeat;

public class FEGame extends AbstractSnakeGame {
    public final int inputs;
    public final int outputs;
    protected List<FESnake> snakes;

    public FEGame(int inputs, int outputs) {
        this.rows = Settings.GRID_ROW_PIXELS / Settings.PIXELS_PER_SQUARE;
        this.columns = Settings.GRID_COLUMN_PIXELS / Settings.PIXELS_PER_SQUARE;
        this.inputs = inputs;
        this.outputs = outputs;
        reset();
    }

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
            createSnake();
        }
    }

    protected void createSnake() {
        var snake = new FESnake(grid);
        snake.genotype = new FEGenotype(inputs, outputs);
        snake.placeSnake();
        snakes.add(snake);
    }

    /**
     * Moves Games.Snake to given direction
     *
     * @param direction where to move snake
     */
    protected void moveSnakeToDirection(FESnake snake, Direction direction) {
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
    protected void moveSnake(FESnake snake, int row, int column) {
        if (!Settings.HAS_WALL) {
            row = wrapAroundCoordinates(row, rows);
            column = wrapAroundCoordinates(column, columns);
        }
        if (snakeCollision(snake, row, column)) {
            var foodPlaced = snake.removeSnake(Settings.LEAVE_CORPSE);
            numOfFood += foodPlaced;
            snakes.remove(snake);
        } else {
            moveSnakeByOne(snake, row, column);
            if (snake.stepsMoved == Settings.STEPS_TO_REDUCTION) {
                snake.stepsMoved = 0;
                snake.reduceSnakeByOne();
                if (snake.size() == 0) {
                    snakes.remove(snake);
                }
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
    protected void moveSnakeByOne(FESnake snake, int row, int column) {
        var bodyParts = snake.bodyParts;

        bodyParts.get(0).isHead = false;
        bodyParts.add(0, new BodyPart(true, row, column));

        if (grid[row][column] == SnakeMap.FOOD.value) {
            snake.placeSnake();
            numOfFood--;
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
    protected boolean snakeCollision(FESnake snake, int row, int column) {
        return grid[row][column] == SnakeMap.WALL.value || snake.isSnakeCollision(row, column);
    }

    /**
     * Plays the game and saves grid arrangements so they can be used later e.g. for visualization.
     */
    public SavedGameDTO saveSnakeMoves() {
        var savedGameDTO = new SavedGameDTO();
        int frameCount = 0;

        for (int move = 0; move < Settings.MAX_NUM_OF_MOVES; move++) {
            frameCount++;
            for (int i = 0; i < snakes.size(); i++) {
                var networkOutput = snakes.get(i).getNeuralNetwork().getNetworkOutput(snakeSightDTO.getInput(snakes.get(i)));
                snakes.get(i).offspringNeuronOutput = networkOutput[4];
                moveSnakeToDirection(snakes.get(i), outputToDirection(networkOutput));
            }
            if (snakes.size() < Settings.NUM_OF_PLAYERS) {
                for (int i = 0; i < Settings.NUM_OF_PLAYERS - snakes.size(); i++) {
                    createSnake();
                }
            }
            savedGameDTO.grid.add(arrayCopy(grid));
            for (int i = 0; i < snakes.size(); i++) {
                produceOffspring(snakes.get(i));
            }
            if (snakes.size() == 0)
                break;
            for (int i = 0; i < Settings.NUM_FOOD_PER_TURN; i++) {
                placeFood();
            }
        }
        setSaveGameMetadata(savedGameDTO);
        savedGameDTO.totalFrames = frameCount;
        return savedGameDTO;
    }

    public void produceOffspring(FESnake snake) {
        if (snake.size() >= Settings.MIN_PARENT_LENGTH_FOR_OFFSPRING && snake.offspringNeuronOutput > Settings.OFFSPRING_THRESHOLD) {
            var offspring = snake.produceOffSpring();
            snakes.add(offspring);
            offspring.placeSnake();
        }
    }

    /**
     * If there is less food on the grid then Settings.maxNumberOfFood one additional food will be added on random grid
     * square.
     */
    @Override
    protected void placeFood() {
        if (numOfFood >= Settings.MAX_NUM_OF_FOOD) {
            return;
        }

        var coordinates = randomFreeCoordinate(grid);
        if (grid[coordinates.getFirst()][coordinates.getSecond()] == SnakeMap.EMPTY.value) {
            grid[coordinates.getFirst()][coordinates.getSecond()] = SnakeMap.FOOD.value;
            numOfFood++;
        }
    }
}
