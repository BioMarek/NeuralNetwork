import Evolution.EvolutionEngine;
import NeuralNetwork.NeuralNetwork;
import NeuralNetwork.Util;
import Snake.SnakeGame;

public class Main {
    public static void main(String[] args) {
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
        NeuralNetwork neuralNetwork = evolutionEngine.getNeuralNetwork(0);
        System.out.println(neuralNetwork.name);
        snakeGame.showSnakeMoves(neuralNetwork, 500);
    }
}
