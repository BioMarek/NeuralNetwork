import Evolution.EvolutionEngine;
import NeuralNetwork.Util;

public class Main {
    public static void main(String[] args) {
        EvolutionEngine evolutionEngine = new EvolutionEngine
                .EvolutionEngineBuilder(1, new int[]{8, 10, 4})
                .hiddenLayerActivationFunc(Util.activationFunctionUnitStep())
                .outputLayerActivationFunc(Util.activationFunctionIdentity())
                .build();

        evolutionEngine.playSnake();
    }
}
