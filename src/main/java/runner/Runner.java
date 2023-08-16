package runner;

import games.snake.SnakeGameMultiplayer;
import games.snake.freeEvolution.FEGame;
import games.snake.savegame.SavedGameDTO;
import neat.evolution.GenePool;
import utils.Settings;
import visualizations.snakeGraphic.videoGeneration.VideoGenerator;

public class Runner {

    public static void runAllGamesInConfiguration() {
        for (var configurator : GameConfiguration.getConfigurations()) {
            configurator.configure();
            if (Settings.IS_FREE_EVOLUTION) {
                playFreeEvolution();
            } else {
                playMultiplayerGame();
            }
        }
    }

    public static void playMultiplayerGame() {
        long start = System.currentTimeMillis();
        GenePool genePool = null;

        switch (Settings.SNAKE_SIGHT_TYPE) {
            case RAYS -> genePool = new GenePool(8, 4, new SnakeGameMultiplayer());
            case TOP_DOWN -> {
                var snakeSight = Settings.SNAKE_SIGHT * 2 + 1;
                genePool = new GenePool(snakeSight * snakeSight, 4, new SnakeGameMultiplayer());
            }
        }

        genePool.calculateEvolution();
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");
    }

    public static void playFreeEvolution() {
        long start = System.currentTimeMillis();
        var feGame = new FEGame(8, 5);
        SavedGameDTO savedGameDTO = feGame.saveSnakeMoves();

        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        var videoGenerator = new VideoGenerator();
        videoGenerator.generateSavedGameVideo(savedGameDTO);
    }
}
