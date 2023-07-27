package runner;

import games.snake.SnakeGameMultiplayer;
import neat.evolution.GenePool;
import utils.Settings;

public class Runner {

    public static void runAllGamesInConfiguration() {
        for (var configurator : GameConfiguration.getConfigurations()) {
            configurator.configure();
            playGame();
        }
    }

    public static void playGame() {
        GenePool genePool = null;

        switch (Settings.SNAKE_SIGHT_TYPE) {
            case RAYS -> genePool = new GenePool(8, 4, new SnakeGameMultiplayer());
            case TOP_DOWN -> {
                var snakeSight = Settings.SNAKE_SIGHT * 2 + 1;
                genePool = new GenePool(snakeSight * snakeSight, 4, new SnakeGameMultiplayer());
            }
        }

        long start = System.currentTimeMillis();
        genePool.calculateEvolution();
        long stop = System.currentTimeMillis();
        System.out.println("It took: " + (stop - start) / 1000 + "s");
    }
}
