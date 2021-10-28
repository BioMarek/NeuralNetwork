import Evolution.EvolutionEngine;
import NeuralNetwork.NeuralNetwork;
import NeuralNetwork.Util;

public class Main {
    public static void main(String[] args) {
        EvolutionEngine evolutionEngine = new EvolutionEngine
                .EvolutionEngineBuilder(new int[]{8, 10, 4}, Util.activationFunctionUnitStep())
                .setTotalNumOfNetworks(1)
                .setOutputLayerActivationFunc(Util.activationFunctionIdentity())
                .build();

        evolutionEngine.playSnake();

        NeuralNetwork neuralNetwork = new NeuralNetwork(new int[]{8, 10, 4}, Util.activationFunctionUnitStep(), Util.activationFunctionUnitStep());
    }
}
