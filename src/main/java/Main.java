import Evolution.EvolutionEngine;
import NeuralNetwork.Util;

public class Main {
    public static void main(String[] args) {
        EvolutionEngine evolutionEngine = new EvolutionEngine
                .EvolutionEngineBuilder(new int[]{8, 10, 4}, Util.activationFunctionUnitStep())
                .setOutputLayerActivationFunc(Util.activationFunctionIdentity())
                .build();

        evolutionEngine.calculateEvolution(50);
    }
}
