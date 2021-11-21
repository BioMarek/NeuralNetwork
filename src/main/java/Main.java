import BasicNeuralNetwork.Evolution.EvolutionEngine;
import BasicNeuralNetwork.NeuralNetwork.BasicNeuralNetwork;
import Games.Snake.SnakeGame;
import NEAT.Evolution.GenePool;
import Utils.Util;

public class Main {
    public static void main(String[] args) {
        setupNeatNeuralNetwork();
    }

    public static void setupNeatNeuralNetwork() {
        GenePool genePool = new GenePool.GenePoolBuilder(8, 4, Util.activationFunctionIdentity(), new SnakeGame(20))
                .build();

        genePool.makeNextGeneration();
    }

    public static void setupBasicNeuralNetwork() {
        EvolutionEngine evolutionEngine = new EvolutionEngine
                .EvolutionEngineBuilder(new int[]{8, 8, 4}, Util.activationFunctionUnitStep(), new SnakeGame(20))
                .setOutputLayerActivationFunc(Util.activationFunctionIdentity())
                .setNumOfNeuronsToMutate(2)
                .setNumOfMutations(2)
                .build();

        long start = System.currentTimeMillis();
        evolutionEngine.calculateEvolution(2000);
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) + "ms");

        SnakeGame snakeGame = new SnakeGame(20);
        BasicNeuralNetwork neuralNetwork = evolutionEngine.getNeuralNetwork(0);
        System.out.println(neuralNetwork.name);
        snakeGame.showSnakeMoves(neuralNetwork, 500);
    }
}
