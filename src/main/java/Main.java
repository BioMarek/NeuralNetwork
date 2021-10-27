import Evolution.EvolutionEngine;
import NeuralNetwork.Util;

public class Main {
    public static void main(String[] args) {
        EvolutionEngine evolutionEngine = new EvolutionEngine(1,
                new int[]{1, 1, 1},
                Util.activationFunctionUnitStep(),
                Util.activationFunctionIdentity());
        evolutionEngine.playSnake();
    }
}
