package Evolution;

import NeuralNetwork.NeuralNetwork;
import Snake.SnakeGame;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvolutionEngine {
    private final List<NeuralNetwork> neuralNetworks = new ArrayList<>();

    public void playSnake() {
        NeuralNetwork nn = neuralNetworks.get(0);
        SnakeGame snakeGame = new SnakeGame(20);

        for (int i = 0; i < 10; i++) {
            double[] networkOutput = nn.getNetworkOutput(snakeGame.snakeMapper().getInput());
            snakeGame.processNeuralNetworkMove(translateOutputToKey(networkOutput));
            if (snakeGame.isGameOver)
                break;
        }
    }

    public String translateOutputToKey(double[] neuralNetworkOutput) {
        int maxIndex = 0;
        double max = neuralNetworkOutput[0];

        for (int i = 0; i < neuralNetworkOutput.length; i++) {
            if (max < neuralNetworkOutput[i]) {
                max = neuralNetworkOutput[i];
                maxIndex = i;
            }
        }

        switch (maxIndex) {
            case 0:
                return "w";
            case 1:
                return "s";
            case 2:
                return "a";
            case 3:
                return "d";
        }

        return "error";
    }

    public void nextGeneration() {

    }

    public static class EvolutionEngineBuilder {
        private final int numOfNetworks;
        private final int[] neuralNetworkSettings;
        private Function<Double, Double> hiddenLayerActivationFunc;
        private Function<Double, Double> outputLayerActivationFunc;

        public EvolutionEngineBuilder(int numOfNetworks, int[] neuralNetworkSettings) {
            this.numOfNetworks = numOfNetworks;
            this.neuralNetworkSettings = neuralNetworkSettings;
        }

        public EvolutionEngineBuilder hiddenLayerActivationFunc(Function<Double, Double> hiddenLayerActivationFunc) {
            this.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
            return this;
        }

        public EvolutionEngineBuilder outputLayerActivationFunc(Function<Double, Double> outputLayerActivationFunc) {
            this.outputLayerActivationFunc = outputLayerActivationFunc;
            return this;
        }

        public EvolutionEngine build() {
            EvolutionEngine evolutionEngine = new EvolutionEngine();

            for (int i = 0; i < numOfNetworks; i++) {
                evolutionEngine.neuralNetworks.add(new NeuralNetwork(neuralNetworkSettings, hiddenLayerActivationFunc, outputLayerActivationFunc));
            }

            return evolutionEngine;
        }
    }
}
