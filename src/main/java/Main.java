import basic_neural_network.evolution.BasicEvolutionEngine;
import basic_neural_network.neural_network.BasicNeuralNetwork;
import games.snake.SnakeGame;
import games.snake.SnakeGameMultiplayer;
import games.snake.savegame.SaveGameUtil;
import interfaces.NeuralNetwork;
import neat.evolution.GenePool;
import utils.Settings;
import utils.Util;
import visualizations.SnakeFrame;
import visualizations.SnakePanel;

public class Main {

    public static void main(String[] args) {
        setupNeatNeuralNetworkWithMultiplayer();
//        setupNeatNeuralNetworkWithGame();

//        playSaveGame("04-21-214845.sav");

    }

    /**
     * Runs {@link SnakeGameMultiplayer} with NEAT network with single player
     */
    public static void setupNeatNeuralNetworkWithMultiplayer() {
        Settings.multiplayerSettings();
        GenePool genePool = new GenePool(8, 4, Util.activationFunctionUnitStep(), new SnakeGameMultiplayer());
//        Settings.singlePlayerGame();
//        GenePool genePool = new GenePool(8, 4, Util.activationFunctionUnitStep(), Util.activationFunctionIdentity(), new SnakeGameMultiplayer());

        long start = System.currentTimeMillis();
        genePool.calculateEvolutionMultiplayer(Settings.NUM_OF_GENERATIONS);
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        SaveGameUtil.saveObjectToFile(genePool.savedGameDTO);
        new SnakeFrame(new SnakePanel(genePool.savedGameDTO));
    }


    /**
     * Runs first version of snake {@link SnakeGame} with NEAT network, saves data used in visualization
     */
    public static void setupNeatNeuralNetworkWithGame() {
        // TODO fix path for visualization
        GenePool genePool = new GenePool(8, 4, Util.activationFunctionUnitStep(), new SnakeGame());

        long start = System.currentTimeMillis();
        genePool.calculateEvolution();
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        SnakeGame snakeGame = new SnakeGame();
        NeuralNetwork neuralNetwork = genePool.getSpecies().get(0).genotypes.get(0).createPhenotype();
        System.out.println(neuralNetwork);
        var savedGameDTO = snakeGame.saveSnakeMoves(neuralNetwork, Settings.MAX_NUM_OF_MOVES);
        SaveGameUtil.saveObjectToFile(savedGameDTO);
        new SnakeFrame(new SnakePanel(savedGameDTO));
    }

    /**
     * Runs first version of snake {@link SnakeGame} with {@link BasicNeuralNetwork}
     */
    public static void setupBasicNeuralNetwork() {
        BasicEvolutionEngine evolutionEngine = new BasicEvolutionEngine
                .EvolutionEngineBuilder(new int[]{8, 8, 4}, Util.activationFunctionUnitStep(), new SnakeGame())
                .setOutputLayerActivationFunc(Util.activationFunctionIdentity())
                .setNumOfNeuronsToMutate(2)
                .setNumOfMutations(2)
                .build();

        long start = System.currentTimeMillis();
        evolutionEngine.calculateEvolution();
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        SnakeGame snakeGame = new SnakeGame();
        BasicNeuralNetwork neuralNetwork = evolutionEngine.getNeuralNetwork(0);
        neuralNetwork.printNetwork();
        System.out.println(neuralNetwork.name);
        snakeGame.showSnakeMoves(neuralNetwork, 500);
    }

    public static void playSaveGame(String filename) {
        var path = Settings.SAVE_GAME_PATH + filename;
        var savedGameDTO = SaveGameUtil.loadObjectFromFile(path);
        new SnakeFrame(new SnakePanel(savedGameDTO));
    }
}
