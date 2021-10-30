import Evolution.EvolutionEngine;
import NeuralNetwork.Util;
import Snake.SnakeGame;

public class Main {
    public static void main(String[] args) {
        EvolutionEngine evolutionEngine = new EvolutionEngine
                .EvolutionEngineBuilder(new int[]{8, 10, 4}, Util.activationFunctionUnitStep(), new SnakeGame(20))
                .setOutputLayerActivationFunc(Util.activationFunctionIdentity())
                .build();

        evolutionEngine.calculateEvolution(50);
    }
}
