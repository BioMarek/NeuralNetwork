import Evolution.EvolutionEngine;
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

        evolutionEngine.calculateEvolution(2000);
    }
}
