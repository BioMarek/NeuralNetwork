import games.freeEvolution.FEGame;
import games.snake.SnakeGameMultiplayer;
import games.snake.savegame.SaveGameUtil;
import games.snake.savegame.SavedGameDTO;
import neat.evolution.GenePool;
import utils.Settings;
import visualizations.snakeGraphic.SnakeFrame;
import visualizations.snakeGraphic.SnakePanel;

public class Main {

    public static void main(String[] args) {
//        setupNeatNeuralNetworkWithMultiplayer();

        setupFreeEvolution();

//        playSaveGame("05-06-202527.sav");

//        var videoGenerator = new VideoGenerator();
//        videoGenerator.generateSavedGameVideo("05-15-195741.sav");

    }

    /**
     * Runs {@link SnakeGameMultiplayer} with NEAT network with single player
     */
    public static void setupNeatNeuralNetworkWithMultiplayer() {
        Settings.multiplayerSettings();
        GenePool genePool = new GenePool(8, 4, new SnakeGameMultiplayer());
//        Settings.singlePlayerGame();
//        GenePool genePool = new GenePool(8, 4, new SnakeGameMultiplayer());

        long start = System.currentTimeMillis();
        genePool.calculateEvolution();
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        SaveGameUtil.saveObjectToFile(genePool.savedGameDTO);
        new SnakeFrame(new SnakePanel(genePool.savedGameDTO));
    }

    public static void setupFreeEvolution() {
        Settings.freeEvolutionSettings();
        long start = System.currentTimeMillis();
        var feGame = new FEGame(8, 4);
        SavedGameDTO savedGameDTO = feGame.saveSnakeMoves();
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        SaveGameUtil.saveObjectToFile(savedGameDTO);
        new SnakeFrame(new SnakePanel(savedGameDTO));
    }

    public static void playSaveGame(String filename) {
        var path = Settings.SAVE_GAME_PATH + filename;
        var savedGameDTO = SaveGameUtil.loadObjectFromFile(path);
        new SnakeFrame(new SnakePanel(savedGameDTO));
    }
}
