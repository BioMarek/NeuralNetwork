package basic_neural_network.evolution;

import basic_neural_network.neural_network.BasicNeuralNetwork;
import games.Game;
import interfaces.EvolutionEngine;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import utils.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicEvolutionEngine implements EvolutionEngine {
    private int[] neuralNetworkSettings;
    private Function<Double, Double> hiddenLayerActivationFunc;
    private Function<Double, Double> outputLayerActivationFunc;

    protected List<BasicNeuralNetwork> neuralNetworks = new ArrayList<>();
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

    @Override
    public void calculateEvolution() {
        for (int i = 0; i < Settings.NUM_OF_GENERATIONS; i++) {
            makeNextGeneration();
        }
    }

    @Override
    public void makeNextGeneration() {
        for (BasicNeuralNetwork neuralNetwork : neuralNetworks) {
            for (int i = 0; i < numOfTrials; i++) {
                game.reset();
                neuralNetwork.score += game.play(neuralNetwork, maxNumberOfMoves);
            }
        }
        neuralNetworks.sort(Collections.reverseOrder());

        if (verbose)
            printScores();
        resetScores();

        List<BasicNeuralNetwork> neuralNetworksNewGeneration = new ArrayList<>();
        for (int i = 0; i < totalNumOfNetworks; i++) {
            if (i < networksToKeep) {
                neuralNetworks.get(i).age++;
                neuralNetworksNewGeneration.add(neuralNetworks.get(i));
            }
            if (i >= networksToKeep && i < (networksToMutate + networksToKeep)) {
                BasicNeuralNetwork neuralNetwork = neuralNetworks.get(i - networksToMutate).copy(); // TODO i - networksToKeep?
                neuralNetwork.mutateLayers(numOfNeuronsToMutate, numOfMutations);
                neuralNetwork.name = Integer.toString(networksGenerated++);
                neuralNetworksNewGeneration.add(neuralNetwork);
            }
            if (i >= networksToMutate + networksToKeep) {
                BasicNeuralNetwork neuralNetwork = new BasicNeuralNetwork(neuralNetworkSettings, hiddenLayerActivationFunc, outputLayerActivationFunc);
                neuralNetwork.name = Integer.toString(networksGenerated++);
                neuralNetworksNewGeneration.add(neuralNetwork);
            }
        }
        neuralNetworks = neuralNetworksNewGeneration;
    }

    @Override
    public void resetScores() {
        for (BasicNeuralNetwork neuralNetwork : neuralNetworks) {
            neuralNetwork.score = 0;
        }
    }

    @Override
    public void printScores() {
        for (BasicNeuralNetwork neuralNetwork : neuralNetworks) {
            System.out.print(neuralNetwork.name + " " + neuralNetwork.age + ": " + neuralNetwork.score * 1.0 / numOfTrials  + ", ");
        }
        System.out.println();
    }

    public BasicNeuralNetwork getNeuralNetwork(int index) {
        return neuralNetworks.get(index);
    }

    public static class EvolutionEngineBuilder {
        private final int[] neuralNetworkSettings;
        private final Game game;
        private final Function<Double, Double> hiddenLayerActivationFunc;
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
            this.numOfTrials = numOfTrials;
            return this;
        }

        public EvolutionEngineBuilder setVerbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        /**
         * Builds evolution engine. {@link BasicNeuralNetwork} will get number names starting from 0 increasing by 1.
         *
         * @return {@link BasicEvolutionEngine}
         */
        public BasicEvolutionEngine build() {
            BasicEvolutionEngine evolutionEngine = new BasicEvolutionEngine();
            evolutionEngine.game = game;
            evolutionEngine.networksGenerated = 0;
            evolutionEngine.currentGenerations = 0;

            for (int i = 0; i < totalNumOfNetworks; i++) {
                BasicNeuralNetwork neuralNetwork = new BasicNeuralNetwork(neuralNetworkSettings, hiddenLayerActivationFunc, outputLayerActivationFunc);
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
