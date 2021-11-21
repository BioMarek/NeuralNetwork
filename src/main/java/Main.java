import BasicNeuralNetwork.Evolution.BasicEvolutionEngine;
import BasicNeuralNetwork.NeuralNetwork.BasicNeuralNetwork;
import Games.Snake.SnakeGame;
import Interfaces.NeuralNetwork;
import NEAT.Evolution.GenePool;
import Utils.Util;

public class Main {
    private static final int NUM_OF_GENERATIONS = 1000;

    public static void main(String[] args) {
        setupNeatNeuralNetwork();
    }

    public static void setupNeatNeuralNetwork() {
        GenePool genePool = new GenePool.GenePoolBuilder(8, 4, Util.activationFunctionIdentity(), new SnakeGame(20))
                .setChanceToMutateWeight(1)
                .setChanceToHardMutateWight(1)
                .setChanceToAddNode(0.03)
                .setNumOfTrials(25)
                .build();

        long start = System.currentTimeMillis();
        genePool.calculateEvolution(NUM_OF_GENERATIONS);
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        SnakeGame snakeGame = new SnakeGame(20);
        NeuralNetwork neuralNetwork = genePool.getGenotypes().get(0).createPhenotype();
        System.out.println(neuralNetwork);
        snakeGame.showSnakeMoves(neuralNetwork, 500);
    }

    public static void setupBasicNeuralNetwork() {
        BasicEvolutionEngine evolutionEngine = new BasicEvolutionEngine
                .EvolutionEngineBuilder(new int[]{8, 8, 4}, Util.activationFunctionUnitStep(), new SnakeGame(20))
                .setOutputLayerActivationFunc(Util.activationFunctionIdentity())
                .setNumOfNeuronsToMutate(2)
                .setNumOfMutations(2)
                .build();

        long start = System.currentTimeMillis();
        evolutionEngine.calculateEvolution(NUM_OF_GENERATIONS);
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) + "ms");

        SnakeGame snakeGame = new SnakeGame(20);
        BasicNeuralNetwork neuralNetwork = evolutionEngine.getNeuralNetwork(0);
        System.out.println(neuralNetwork.name);
        snakeGame.showSnakeMoves(neuralNetwork, 500);
    }
}
