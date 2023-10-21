package runner;

import games.snake.SnakeGameMultiplayer;
import games.snake.freeEvolution.FEGame;
import games.snake.savegame.SavedGameDTO;
import neat.evolution.GenePool;
import utils.Settings;
import utils.Util;
import visualizations.snakeGraphic.videoGeneration.VideoGenerator;

public class Runner {

    public static void runAllGamesInConfiguration() {
        for (var configurator : GameConfiguration.getConfigurations()) {
            configurator.configure();
            printSettings();
            Settings.HASH = Util.generateRandomString(4);
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
        printFESettings();
        long start = System.currentTimeMillis();
        var feGame = new FEGame(8, 5);
        SavedGameDTO savedGameDTO = feGame.saveSnakeMoves();

        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");

        var videoGenerator = new VideoGenerator();
        videoGenerator.generateSavedGameVideo(savedGameDTO);
    }

    public static void printSettings() {
        System.out.println("NUM_OF_PLAYERS " + Settings.NUM_OF_PLAYERS);
        System.out.println("PIXELS_PER_SQUARE " + Settings.PIXELS_PER_SQUARE);
        System.out.println("DEATH_PENALTY " + Settings.DEATH_PENALTY);
        System.out.println("MAX_NUM_OF_FOOD " + Settings.MAX_NUM_OF_FOOD);
        System.out.println("STEPS_TO_REDUCTION " + Settings.STEPS_TO_REDUCTION);
        System.out.println("LEAVE_CORPSE " + Settings.LEAVE_CORPSE);
        System.out.println("HAS_WALL " + Settings.HAS_WALL);
        System.out.println("SELF_COLLISION " + Settings.SELF_COLLISION);
    }

    public static void printFESettings() {
        System.out.println("CHANCE_MUTATE_WEIGHT " + Settings.CHANCE_MUTATE_WEIGHT);
        System.out.println("CHANCE_HARD_MUTATE_WEIGHT " + Settings.CHANCE_HARD_MUTATE_WEIGHT);
        System.out.println("CHANCE_SWITCH_CONNECTION_ENABLED " + Settings.CHANCE_SWITCH_CONNECTION_ENABLED);
        System.out.println("CHANCE_ADD_NODE " + Settings.CHANCE_ADD_NODE);
        System.out.println("CHANCE_ADD_CONNECTION " + Settings.CHANCE_ADD_CONNECTION);
        System.out.println("IS_FOOD_GUARANTEED " + Settings.IS_FOOD_GUARANTEED);
        System.out.println("OFFSPRING_THRESHOLD " + Settings.OFFSPRING_THRESHOLD);
        System.out.println("NUM_FOOD_PER_TURN " + Settings.NUM_FOOD_PER_TURN);
    }
}
