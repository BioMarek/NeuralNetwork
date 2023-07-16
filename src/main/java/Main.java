import games.freeEvolution.FEGame;
import games.snake.SnakeGameIntro;
import games.snake.SnakeGameMultiplayer;
import games.snake.savegame.SaveGameUtil;
import games.snake.savegame.SavedGameDTO;
import neat.evolution.GenePool;
import utils.Settings;
import visualizations.snakeGraphic.SnakeFrame;
import visualizations.snakeGraphic.SnakePanel;

public class Main {

    public static void main(String[] args) {
        setupNeatNeuralNetworkWithMultiplayer();
//        setupNeatNeuralNetworkWithMultiplayerIntro();

//        setupFreeEvolution();

//        Settings.multiplayerSettings();
//        playSaveGame("06-04-212719.sav");

//        Settings.explanationSettings();
//        var videoGenerator = new VideoGenerator();
////        videoGenerator.generateSnakeIntroduction();
//        videoGenerator.generateSnakeIntroSnakeGame();

        //        videoGenerator.generateSavedGameVideo("06-05-205714.sav");

    }

    /**
     * Runs {@link SnakeGameMultiplayer} with NEAT network with single player
     */
    public static void setupNeatNeuralNetworkWithMultiplayer() {
        Settings.multiplayerSettingsTopDown();
        GenePool genePool = null;

        switch (Settings.SNAKE_SIGHT_TYPE) {
            case RAYS -> genePool = new GenePool(8, 4, new SnakeGameMultiplayer());
            case TOP_DOWN -> {
                var snakeSight = Settings.SNAKE_SIGHT * 2 + 1;
                genePool = new GenePool(snakeSight * snakeSight, 4, new SnakeGameMultiplayer());
            }
        }
//        Settings.singlePlayerGame();
//        GenePool genePool = new GenePool(8, 4, new SnakeGameMultiplayer());

        long start = System.currentTimeMillis();
        genePool.calculateEvolution();
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

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

    public static void setupNeatNeuralNetworkWithMultiplayerIntro() {
        Settings.introSettings();
        GenePool genePool = new GenePool(8, 4, new SnakeGameIntro());
//        Settings.singlePlayerGame();
//        GenePool genePool = new GenePool(8, 4, new SnakeGameMultiplayer());

        long start = System.currentTimeMillis();
        genePool.calculateEvolution();
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        new SnakeFrame(new SnakePanel(genePool.savedGameDTO));
    }
}
