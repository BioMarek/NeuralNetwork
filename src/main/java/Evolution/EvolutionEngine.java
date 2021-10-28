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
    private int totalNumOfNetworks;
    private int networksGenerated;
    private int networksToKeep; // number of top scoring networks that are copied into next generation
    private int networksToMutate; // number of top scoring networks copies that are mutated and copied into next generation
    private int currentGenerations;

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
        private final int[] neuralNetworkSettings;
        private Function<Double, Double> hiddenLayerActivationFunc;
        private Function<Double, Double> outputLayerActivationFunc;
        private int totalNumOfNetworks = 100;
        private int networksToKeep = 40;
        private int networksToMutate = 40;

        public EvolutionEngineBuilder(int[] neuralNetworkSettings, Function<Double, Double> hiddenLayerActivationFunc) {
            this.neuralNetworkSettings = neuralNetworkSettings;
            this.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
            this.outputLayerActivationFunc = hiddenLayerActivationFunc;
        }

        public EvolutionEngineBuilder setTotalNumOfNetworks(int totalNumOfNetworks) {
            this.totalNumOfNetworks = totalNumOfNetworks;
            return this;
        }

        public EvolutionEngineBuilder setOutputLayerActivationFunc(Function<Double, Double> outputLayerActivationFunc) {
            this.outputLayerActivationFunc = outputLayerActivationFunc;
            return this;
        }

        public EvolutionEngineBuilder setNetworksToKeep(int networksToKeep) {
            this.networksToKeep = networksToKeep;
            return this;
        }

        public EvolutionEngineBuilder setNetworksToMutate(int networksToMutate) {
            this.networksToMutate = networksToMutate;
            return this;
        }

        /**
         * Builds evolution engine. {@link NeuralNetwork} will get number names starting from 0 increasing by 1.
         *
         * @return {@link EvolutionEngine}
         */
        public EvolutionEngine build() {
            EvolutionEngine evolutionEngine = new EvolutionEngine();
            evolutionEngine.networksGenerated = 0;

            for (int i = 0; i < totalNumOfNetworks; i++) {
                NeuralNetwork neuralNetwork = new NeuralNetwork(neuralNetworkSettings, hiddenLayerActivationFunc, outputLayerActivationFunc);
                neuralNetwork.name = Integer.toString(evolutionEngine.networksGenerated++);
                evolutionEngine.neuralNetworks.add(neuralNetwork);
            }

            evolutionEngine.totalNumOfNetworks = this.totalNumOfNetworks;
            evolutionEngine.networksToKeep = this.networksToKeep;
            evolutionEngine.networksToMutate = this.networksToMutate;

            evolutionEngine.currentGenerations = 0;

            return evolutionEngine;
        }
    }
}
