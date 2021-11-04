package BasicNeuralNetwork.Evolution;

import Games.Game;
import BasicNeuralNetwork.NeuralNetwork.NeuralNetwork;
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
    protected int numOfNeuronsToMutate;
    protected int numOfMutations;
    protected int numOfTrials; // how many times BasicNeuralNetwork.NeuralNetwork plays the game
    protected Game game;
    protected boolean verbose;

    public void calculateEvolution(int numOfGenerations) {
        for (int i = 0; i < numOfGenerations; i++) {
            makeNextGeneration();
        }
    }

    public void makeNextGeneration() {
        for (NeuralNetwork neuralNetwork : neuralNetworks) {
            for (int i = 0; i < numOfTrials; i++) {
                game.reset();
                game.play(neuralNetwork, maxNumberOfMoves);
            }
        }
        neuralNetworks.sort(Collections.reverseOrder());
        if (verbose)
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
                neuralNetwork.mutateLayers(numOfNeuronsToMutate, numOfMutations);
                neuralNetwork.name = Integer.toString(networksGenerated++);
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

    public NeuralNetwork getNeuralNetwork(int index) {
        return neuralNetworks.get(index);
    }

    public static class EvolutionEngineBuilder {
        private final int[] neuralNetworkSettings;
        private final Game game;
        private Function<Double, Double> hiddenLayerActivationFunc;
        private Function<Double, Double> outputLayerActivationFunc;
        private int totalNumOfNetworks = 100;
        private int networksToKeep = 40;
        private int networksToMutate = 40;
        private int maxNumberOfMoves = 500;
        private int numOfNeuronsToMutate = 1;
        private int numOfMutations = 1;
        private int numOfTrials = 10;
        protected boolean verbose = true;

        public EvolutionEngineBuilder(int[] neuralNetworkSettings, Function<Double, Double> hiddenLayerActivationFunc, Game game) {
            this.neuralNetworkSettings = neuralNetworkSettings;
            this.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
            this.outputLayerActivationFunc = hiddenLayerActivationFunc;
            this.game = game;
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

        public EvolutionEngineBuilder setNumOfNeuronsToMutate(int numOfNeuronsToMutate) {
            this.numOfNeuronsToMutate = numOfNeuronsToMutate;
            return this;
        }

        public EvolutionEngineBuilder setNumOfMutations(int numOfMutations) {
            this.numOfMutations = numOfMutations;
            return this;
        }

        public EvolutionEngineBuilder setNumOfTrials(int numOfTrials) {
            this.numOfMutations = numOfTrials;
            return this;
        }

        public EvolutionEngineBuilder setVerbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
         * Builds evolution engine. {@link NeuralNetwork} will get number names starting from 0 increasing by 1.
         *
         * @return {@link EvolutionEngine}
         */
        public EvolutionEngine build() {
            EvolutionEngine evolutionEngine = new EvolutionEngine();
            evolutionEngine.game = game;
            evolutionEngine.networksGenerated = 0;
            evolutionEngine.currentGenerations = 0;

            for (int i = 0; i < totalNumOfNetworks; i++) {
                NeuralNetwork neuralNetwork = new NeuralNetwork(neuralNetworkSettings, hiddenLayerActivationFunc, outputLayerActivationFunc);
                neuralNetwork.name = Integer.toString(evolutionEngine.networksGenerated++);
                evolutionEngine.neuralNetworks.add(neuralNetwork);
            }

            evolutionEngine.neuralNetworkSettings = neuralNetworkSettings;
            evolutionEngine.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
            evolutionEngine.outputLayerActivationFunc = outputLayerActivationFunc;
            evolutionEngine.totalNumOfNetworks = totalNumOfNetworks;
            evolutionEngine.networksToKeep = networksToKeep;
            evolutionEngine.networksToMutate = networksToMutate;
            evolutionEngine.maxNumberOfMoves = maxNumberOfMoves;
            evolutionEngine.numOfNeuronsToMutate = numOfNeuronsToMutate;
            evolutionEngine.numOfMutations = numOfMutations;
            evolutionEngine.numOfTrials = numOfTrials;
            evolutionEngine.verbose = verbose;
            return evolutionEngine;
        }
    }
}
