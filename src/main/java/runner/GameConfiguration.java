package runner;

import games.snake.dtos.SnakeSightType;
import utils.Settings;

import java.util.ArrayList;
import java.util.List;

public class GameConfiguration {

    public static List<Configurator> getConfigurations() {
        List<Configurator> configurations = new ArrayList<>();
        configurations.add(multiplayerSettings());
        configurations.add(multiplayerSettings2());
        return configurations;
    }

    public static Configurator multiplayerSettings() {
        return () -> {
            Settings.NUM_OF_PLAYERS = 10;
            Settings.GRID_COLUMN_PIXELS = 1500;
            Settings.GRID_ROW_PIXELS = 1080;
            Settings.PIXELS_PER_SQUARE = 20;
            Settings.SNAKE_SIGHT = 7;
            Settings.DEATH_PENALTY = -3;
            Settings.MAX_NUM_OF_FOOD = 150;
            Settings.LEAVE_CORPSE = false;
            Settings.HAS_WALL = true;
            Settings.SELF_COLLISION = false;
            Settings.SAVE_EVERY_N_GENERATIONS = 100;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
        };
    }

    public static Configurator multiplayerSettings2() {
        return () -> {
            Settings.NUM_OF_PLAYERS = 2;
            Settings.GRID_COLUMN_PIXELS = 1500;
            Settings.GRID_ROW_PIXELS = 1080;
            Settings.PIXELS_PER_SQUARE = 20;
            Settings.SNAKE_SIGHT = 7;
            Settings.DEATH_PENALTY = -3;
            Settings.MAX_NUM_OF_FOOD = 150;
            Settings.LEAVE_CORPSE = false;
            Settings.HAS_WALL = true;
            Settings.SELF_COLLISION = false;
            Settings.SAVE_EVERY_N_GENERATIONS = 100;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
        };
    }
}
