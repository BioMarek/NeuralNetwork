package games.snake.freeEvolution;

import games.snake.AbstractSnake;
import games.snake.BodyPart;
import neat.phenotype.NeuralNetwork;
import utils.Direction;
import utils.Util;

public class FESnake extends AbstractSnake {
    public static int names = 0;
    public FEGenotype genotype;
    public int id;
    public double offspringNeuronOutput;

    public FESnake(int[][] grid, int row, int column, Direction direction, int id, int length) {
        resetSnake(row, column, direction, length);
        this.grid = grid;
        this.stepsMoved = 0;
        this.color = id % 25;
        this.id = id;
    }

    public FESnake(int[][] grid) {
        var coordinates = Util.randomFreeCoordinate(grid);
        resetSnake(coordinates.getFirst(), coordinates.getSecond(), Direction.NONE, 3);
        this.grid = grid;
        this.stepsMoved = 0;
        this.color = names++ % 25;
        this.id = names;
    }

    /**
     * Removes one {@link BodyPart}. Used to simulate starvation
     */
    public void reduceSnakeByOne() {
        removeSnake(false);
        bodyParts.remove(bodyParts.size() - 1);
        placeSnake();
    }

    public FESnake produceOffSpring() {
        removeSnake(false);
        var lastBodypart = bodyParts.get(bodyParts.size() - 1);
        var mutatedGenotype = genotype.getMutatedCopy();
        var newId = id;
        if (mutatedGenotype.newSpecies)
            newId++;
        var offspringCost = bodyParts.size() / 2;
        var offspring = new FESnake(grid, lastBodypart.row, lastBodypart.column, Direction.opposite(lastDirection), newId, offspringCost);
        offspring.genotype = mutatedGenotype;
        offspring.genotype.newSpecies = false;

        for (int i = 0; i < offspringCost; i++) {
            bodyParts.remove(bodyParts.size() - 1);
        }
        return offspring;
    }

    public NeuralNetwork getNeuralNetwork() {
        return genotype.createPhenotype();
    }
}
