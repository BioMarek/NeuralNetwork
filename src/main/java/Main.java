import basic_neural_network.evolution.BasicEvolutionEngine;
import basic_neural_network.neural_network.BasicNeuralNetwork;
import games.snake.SnakeGame;
import games.snake.SnakeGameMultiplayer;
import games.snake.dtos.SavedGameDTO;
import interfaces.NeuralNetwork;
import neat.evolution.GenePool;
import neat.evolution.Genotype;
import utils.Settings;
import utils.Util;
import visualizations.NetworkVisualizationFrame;
import visualizations.SnakeFrame;
import visualizations.SnakePanel;

public class Main {

    public static void main(String[] args) {
//        setupNeatNeuralNetworkMultiplayer();
        snakeVisualization();
//        visualization();
    }

    public static void visualization() {
        GenePool genePool = new GenePool(2, 2, Util.activationFunctionIdentity(), new SnakeGame());
        Genotype genotype = genePool.getSpecies().get(0).genotypes.get(0);
        genotype.addNode(genotype.connectionGenes.get(0));
        genotype.addNode(genotype.connectionGenes.get(0));
        new NetworkVisualizationFrame(genotype.createPhenotype().getVisualizationDTO());
    }

    public static void setupNeatNeuralNetwork() {
        GenePool genePool = new GenePool(8, 4, Util.activationFunctionUnitStep(), new SnakeGame());

        long start = System.currentTimeMillis();
        genePool.calculateEvolution(Settings.numOfGenerations);
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        SnakeGame snakeGame = new SnakeGame();
        NeuralNetwork neuralNetwork = genePool.getSpecies().get(0).genotypes.get(0).createPhenotype();
        System.out.println(neuralNetwork);
        new NetworkVisualizationFrame(neuralNetwork.getVisualizationDTO());
        snakeGame.showSnakeMoves(neuralNetwork, 500);
    }

    public static void setupNeatNeuralNetworkMultiplayer() {
        Settings.multiplayerSettings();
        GenePool genePool = new GenePool(8, 4, Util.activationFunctionUnitStep(), new SnakeGameMultiplayer());

        long start = System.currentTimeMillis();
        genePool.calculateEvolutionMultiplayer(Settings.numOfGenerations);
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");
    }

    public static void setupBasicNeuralNetwork() {
        BasicEvolutionEngine evolutionEngine = new BasicEvolutionEngine
                .EvolutionEngineBuilder(new int[]{8, 8, 4}, Util.activationFunctionUnitStep(), new SnakeGame())
                .setOutputLayerActivationFunc(Util.activationFunctionIdentity())
                .setNumOfNeuronsToMutate(2)
                .setNumOfMutations(2)
                .build();

        long start = System.currentTimeMillis();
        evolutionEngine.calculateEvolution(Settings.numOfGenerations);
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        SnakeGame snakeGame = new SnakeGame();
        BasicNeuralNetwork neuralNetwork = evolutionEngine.getNeuralNetwork(0);
        neuralNetwork.printNetwork();
        System.out.println(neuralNetwork.name);
        snakeGame.showSnakeMoves(neuralNetwork, 500);
    }

    public static void snakeVisualization() {
        Settings.multiplayerSettings();
        GenePool genePool = new GenePool(8, 4, Util.activationFunctionUnitStep(), Util.activationFunctionHyperbolicTangent(), new SnakeGameMultiplayer());

        long start = System.currentTimeMillis();
        genePool.calculateEvolutionMultiplayer(Settings.numOfGenerations);
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        new SnakeFrame(new SnakePanel(genePool.savedGameDTO));

    }
}
