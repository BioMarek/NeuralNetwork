import games.snake.SnakeGameMultiplayer;
import games.snake.savegame.SaveGameUtil;
import neat.evolution.GenePool;
import utils.Settings;
import utils.Util;
import visualizations.snakeGraphic.SnakeFrame;
import visualizations.snakeGraphic.SnakePanel;

public class Main {

    public static void main(String[] args) {
        setupNeatNeuralNetworkWithMultiplayer();
//        setupNeatNeuralNetworkWithGame();

//        playSaveGame("05-06-202527.sav");

    }

    /**
     * Runs {@link SnakeGameMultiplayer} with NEAT network with single player
     */
    public static void setupNeatNeuralNetworkWithMultiplayer() {
        Settings.multiplayerSettings();
        GenePool genePool = new GenePool(8, 4, Util.activationFunctionHyperbolicTangent(), Util.activationFunctionHyperbolicTangent(), new SnakeGameMultiplayer());
//        Settings.singlePlayerGame();
//        GenePool genePool = new GenePool(8, 4, Util.activationFunctionHyperbolicTangent(), Util.activationFunctionHyperbolicTangent(), new SnakeGameMultiplayer());

        long start = System.currentTimeMillis();
        genePool.calculateEvolution(Settings.NUM_OF_GENERATIONS);
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        SaveGameUtil.saveObjectToFile(genePool.savedGameDTO);
        new SnakeFrame(new SnakePanel(genePool.savedGameDTO));
    }

    public static void playSaveGame(String filename) {
        var path = Settings.SAVE_GAME_PATH + filename;
        var savedGameDTO = SaveGameUtil.loadObjectFromFile(path);
        new SnakeFrame(new SnakePanel(savedGameDTO));
    }
}
