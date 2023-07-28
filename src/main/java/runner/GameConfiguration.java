package runner;

import games.snake.dtos.SnakeSightType;
import utils.Settings;

import java.util.ArrayList;
import java.util.List;

public class GameConfiguration {

    public static List<Configurator> getConfigurations() {
        List<Configurator> configurations = new ArrayList<>();
        configurations.add(multiplayerSettings());
//        configurations.add(multiplayerSettings2());
        configurations.add(freeEvolutionSettings());
        return configurations;
    }

    public static Configurator multiplayerSettings() {
        return () -> {
            Settings.NUM_OF_GENERATIONS = 300;
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
            return false;
        };
    }

    public static Configurator multiplayerSettings2() {
        return () -> {
            Settings.NUM_OF_GENERATIONS = 300;
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
            return false;
        };
    }

    public static Configurator freeEvolutionSettings() {
        return () -> {
            Settings.SHOW_LEGEND = false;
            Settings.GRID_COLUMN_PIXELS = 1920;
            Settings.PIXELS_PER_SQUARE = 20;
            Settings.NUM_OF_PLAYERS = 25;
            Settings.CHANCE_MUTATE_WEIGHT = 0.8d;
            Settings.CHANCE_HARD_MUTATE_WEIGHT = 0.1d;
            Settings.CHANCE_SWITCH_CONNECTION_ENABLED = 0.2d;
            Settings.CHANCE_ADD_NODE = 0.03d;
            Settings.CHANCE_ADD_CONNECTION = 0.03d;
            Settings.MAX_NUM_OF_FOOD = 200;
            Settings.LEAVE_CORPSE = false;
            Settings.HAS_WALL = false;
            Settings.SELF_COLLISION = true;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.IS_FOOD_GUARANTEED = false;
            Settings.OFFSPRING_THRESHOLD = 0.15d;
            Settings.MAX_NUM_OF_MOVES = 1000;
            Settings.MAX_NUM_OF_MOVES_VIDEO = 1000;
            return true;
        };
    }
}
