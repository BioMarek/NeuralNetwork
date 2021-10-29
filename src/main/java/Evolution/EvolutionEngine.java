package Evolution;

import NeuralNetwork.NeuralNetwork;
import Snake.SnakeGame;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvolutionEngine {
    private int[] neuralNetworkSettings;
    private Function<Double, Double> hiddenLayerActivationFunc;
    private Function<Double, Double> outputLayerActivationFunc;
    protected List<NeuralNetwork> neuralNetworks = new ArrayList<>();
    protected int totalNumOfNetworks;
    protected int networksGenerated;
    protected int networksToKeep; // number of top scoring networks that are copied into next generation
    protected int networksToMutate; // number of top scoring networks copies that are mutated and copied into next generation
    protected int currentGenerations;
    protected int maxNumberOfMoves; // to stop AI moving in cycles

    /**
     * Plays one game of {@link SnakeGame} with one {@link NeuralNetwork}
     *
     * @param neuralNetwork that plays game
     */
    public void playSnake(NeuralNetwork neuralNetwork) {
        SnakeGame snakeGame = new SnakeGame(20);
        double[] networkOutput;

        for (int i = 0; i < maxNumberOfMoves; i++) {
            networkOutput = neuralNetwork.getNetworkOutput(snakeGame.snakeMapper().getInput());
            snakeGame.processNeuralNetworkMove(translateOutputToKey(networkOutput));
            if (snakeGame.isGameOver)
                break;
        }
        neuralNetwork.score = snakeGame.snakeScore;
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

    public void calculateEvolution(int numOfGenerations) {
        for (int i = 0; i < numOfGenerations; i++) {
            nextGeneration();
        }
    }

    public void nextGeneration() {
        for (NeuralNetwork neuralNetwork : neuralNetworks) {
            playSnake(neuralNetwork);
        }
        neuralNetworks.sort(Collections.reverseOrder());
        printScores();
        for (NeuralNetwork neuralNetwork : neuralNetworks) {
            neuralNetwork.score = 0;
        }
        List<NeuralNetwork> neuralNetworksNewGeneration = new ArrayList<>();
        for (int i = 0; i < totalNumOfNetworks; i++) {
            if (i < networksToKeep) {
                neuralNetworksNewGeneration.add(neuralNetworks.get(i));
            }
            if (i >= networksToKeep && i < (networksToMutate + networksToKeep)) {
                NeuralNetwork neuralNetwork = neuralNetworks.get(i - networksToMutate).copy();
                neuralNetwork.mutateLayers(1, 1);
                neuralNetworksNewGeneration.add(neuralNetwork);
            }
            if (i >= networksToMutate + networksToKeep) {
                NeuralNetwork neuralNetwork = new NeuralNetwork(neuralNetworkSettings, hiddenLayerActivationFunc, outputLayerActivationFunc);
                neuralNetwork.name = Integer.toString(networksGenerated++);
                neuralNetworksNewGeneration.add(neuralNetwork);
            }
        }
        neuralNetworks = neuralNetworksNewGeneration;
    }

    public void printScores() {
        for (NeuralNetwork neuralNetwork : neuralNetworks) {
            System.out.print(neuralNetwork.name + ": " + neuralNetwork.score + ", ");
        }
        System.out.println();
    }

    public static class EvolutionEngineBuilder {
        private final int[] neuralNetworkSettings;
        private Function<Double, Double> hiddenLayerActivationFunc;
        private Function<Double, Double> outputLayerActivationFunc;
        private int totalNumOfNetworks = 100;
        private int networksToKeep = 40;
        private int networksToMutate = 40;
        private int maxNumberOfMoves = 500;

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

        public EvolutionEngineBuilder setMaxNumberOfMoves(int maxNumberOfMoves) {
            this.maxNumberOfMoves = maxNumberOfMoves;
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
            evolutionEngine.currentGenerations = 0;

            for (int i = 0; i < totalNumOfNetworks; i++) {
                NeuralNetwork neuralNetwork = new NeuralNetwork(neuralNetworkSettings, hiddenLayerActivationFunc, outputLayerActivationFunc);
                neuralNetwork.name = Integer.toString(evolutionEngine.networksGenerated++);
                evolutionEngine.neuralNetworks.add(neuralNetwork);
            }

            evolutionEngine.neuralNetworkSettings = this.neuralNetworkSettings;
            evolutionEngine.hiddenLayerActivationFunc = this.hiddenLayerActivationFunc;
            evolutionEngine.outputLayerActivationFunc = this.outputLayerActivationFunc;
            evolutionEngine.totalNumOfNetworks = this.totalNumOfNetworks;
            evolutionEngine.networksToKeep = this.networksToKeep;
            evolutionEngine.networksToMutate = this.networksToMutate;
            evolutionEngine.maxNumberOfMoves = this.maxNumberOfMoves;
            return evolutionEngine;
        }
    }
}
