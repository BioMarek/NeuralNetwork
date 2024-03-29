package runner;

import games.snake.dtos.SnakeSightType;
import utils.Settings;
import utils.Util;

import java.util.ArrayList;
import java.util.List;

public class GameConfiguration {

    public static List<Configurator> getConfigurations() {
        List<Configurator> configurations = new ArrayList<>();
//        configurations.add(multiplayerSettings());
//        configurations.add(multiplayerSettings2PlayersBasic());
//        configurations.add(multiplayerSettings2PlayersSmallGrid());
//        configurations.add(multiplayerSettings2PlayersSmallGridHighPenalty());
//        configurations.add(freeEvolutionSettings());

//        configurations.add(multiplayerRandomSettings());
//        configurations.add(multiplayerRandomSettings());
//        configurations.add(multiplayerRandomSettings());
//        configurations.add(multiplayerRandomSettings());
//        configurations.add(multipleSnakesFillingBoard());
//        configurations.add(multipleSnakesFillingBoard2());
//        configurations.add(multipleSnakesFillingBoard3());

        configurations.add(freeEvolutionRandomSettings());
        configurations.add(freeEvolutionRandomSettings());
        configurations.add(freeEvolutionRandomSettings());
        configurations.add(freeEvolutionRandomSettings());
        configurations.add(freeEvolutionRandomSettings());

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
            Settings.STEPS_TO_REDUCTION = 15;
            Settings.LEAVE_CORPSE = false;
            Settings.HAS_WALL = true;
            Settings.SELF_COLLISION = false;
            Settings.SAVE_EVERY_N_GENERATIONS = 100;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.IS_FREE_EVOLUTION = false;
        };
    }

    public static Configurator multiplayerSettingsNoWall() {
        return () -> {
            Settings.NUM_OF_GENERATIONS = 300;
            Settings.NUM_OF_PLAYERS = 10;
            Settings.GRID_COLUMN_PIXELS = 1500;
            Settings.GRID_ROW_PIXELS = 1080;
            Settings.PIXELS_PER_SQUARE = 20;
            Settings.SNAKE_SIGHT = 7;
            Settings.DEATH_PENALTY = -10;
            Settings.MAX_NUM_OF_FOOD = 150;
            Settings.STEPS_TO_REDUCTION = 1000;
            Settings.LEAVE_CORPSE = false;
            Settings.HAS_WALL = false;
            Settings.SELF_COLLISION = false;
            Settings.SAVE_EVERY_N_GENERATIONS = 100;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.IS_FREE_EVOLUTION = false;
        };
    }

    public static Configurator multiplayerSettingsHighDeathPenalty() {
        return () -> {
            Settings.NUM_OF_GENERATIONS = 300;
            Settings.NUM_OF_PLAYERS = 10;
            Settings.GRID_COLUMN_PIXELS = 1500;
            Settings.GRID_ROW_PIXELS = 1080;
            Settings.PIXELS_PER_SQUARE = 20;
            Settings.SNAKE_SIGHT = 7;
            Settings.DEATH_PENALTY = -100;
            Settings.MAX_NUM_OF_FOOD = 150;
            Settings.STEPS_TO_REDUCTION = 1000;
            Settings.LEAVE_CORPSE = true;
            Settings.HAS_WALL = true;
            Settings.SELF_COLLISION = false;
            Settings.SAVE_EVERY_N_GENERATIONS = 100;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.IS_FREE_EVOLUTION = false;
        };
    }

    public static Configurator multiplayerSettings2PlayersBasic() {
        return () -> {
            Settings.NUM_OF_GENERATIONS = 1000;
            Settings.NUM_OF_PLAYERS = 6;
            Settings.GRID_COLUMN_PIXELS = 1500;
            Settings.GRID_ROW_PIXELS = 1080;
            Settings.PIXELS_PER_SQUARE = 40;
            Settings.SNAKE_SIGHT = 7;
            Settings.DEATH_PENALTY = -3;
            Settings.MAX_NUM_OF_FOOD = 15;
            Settings.STEPS_TO_REDUCTION = 15;
            Settings.LEAVE_CORPSE = false;
            Settings.HAS_WALL = true;
            Settings.SELF_COLLISION = true;
            Settings.SAVE_EVERY_N_GENERATIONS = 1000;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.IS_FREE_EVOLUTION = false;
        };
    }

    public static Configurator multiplayerSettings2PlayersSmallGrid() {
        return () -> {
            Settings.NUM_OF_GENERATIONS = 1000;
            Settings.NUM_OF_PLAYERS = 6;
            Settings.GRID_COLUMN_PIXELS = 1500;
            Settings.GRID_ROW_PIXELS = 1080;
            Settings.PIXELS_PER_SQUARE = 60;
            Settings.SNAKE_SIGHT = 7;
            Settings.DEATH_PENALTY = -3;
            Settings.MAX_NUM_OF_FOOD = 7;
            Settings.STEPS_TO_REDUCTION = 15;
            Settings.LEAVE_CORPSE = false;
            Settings.HAS_WALL = true;
            Settings.SELF_COLLISION = true;
            Settings.SAVE_EVERY_N_GENERATIONS = 1000;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.IS_FREE_EVOLUTION = false;
        };
    }

    public static Configurator multiplayerSettings2PlayersSmallGridHighPenalty() {
        return () -> {
            Settings.NUM_OF_GENERATIONS = 1000;
            Settings.NUM_OF_PLAYERS = 6;
            Settings.GRID_COLUMN_PIXELS = 1500;
            Settings.GRID_ROW_PIXELS = 1080;
            Settings.PIXELS_PER_SQUARE = 60;
            Settings.SNAKE_SIGHT = 7;
            Settings.DEATH_PENALTY = -30;
            Settings.MAX_NUM_OF_FOOD = 7;
            Settings.STEPS_TO_REDUCTION = 15;
            Settings.LEAVE_CORPSE = false;
            Settings.HAS_WALL = true;
            Settings.SELF_COLLISION = true;
            Settings.SAVE_EVERY_N_GENERATIONS = 1000;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.IS_FREE_EVOLUTION = false;
        };
    }

    public static Configurator multipleSnakesFillingBoard() {
        return () -> {
            Settings.NUM_OF_GENERATIONS = 1000;
            Settings.NUM_OF_PLAYERS = 6;
            Settings.GRID_COLUMN_PIXELS = 1500;
            Settings.GRID_ROW_PIXELS = 1080;
            Settings.PIXELS_PER_SQUARE = 40;
            Settings.SNAKE_SIGHT = 7;
            Settings.DEATH_PENALTY = -30;
            Settings.MAX_NUM_OF_FOOD = 100;
            Settings.STEPS_TO_REDUCTION = 15;
            Settings.LEAVE_CORPSE = true;
            Settings.HAS_WALL = false;
            Settings.SELF_COLLISION = false;
            Settings.SAVE_EVERY_N_GENERATIONS = 200;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.IS_FREE_EVOLUTION = false;
        };
    }

    public static Configurator multipleSnakesFillingBoard2() {
        return () -> {
            Settings.NUM_OF_GENERATIONS = 1000;
            Settings.NUM_OF_PLAYERS = 12;
            Settings.GRID_COLUMN_PIXELS = 1500;
            Settings.GRID_ROW_PIXELS = 1080;
            Settings.PIXELS_PER_SQUARE = 30;
            Settings.SNAKE_SIGHT = 7;
            Settings.DEATH_PENALTY = -75;
            Settings.MAX_NUM_OF_FOOD = 150;
            Settings.STEPS_TO_REDUCTION = 15;
            Settings.LEAVE_CORPSE = false;
            Settings.HAS_WALL = true;
            Settings.SELF_COLLISION = false;
            Settings.SAVE_EVERY_N_GENERATIONS = 200;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.IS_FREE_EVOLUTION = false;
        };
    }

    public static Configurator multipleSnakesFillingBoard3() {
        return () -> {
            Settings.NUM_OF_GENERATIONS = 1000;
            Settings.NUM_OF_PLAYERS = 12;
            Settings.GRID_COLUMN_PIXELS = 1500;
            Settings.GRID_ROW_PIXELS = 1080;
            Settings.PIXELS_PER_SQUARE = 30;
            Settings.SNAKE_SIGHT = 7;
            Settings.DEATH_PENALTY = -75;
            Settings.MAX_NUM_OF_FOOD = 150;
            Settings.STEPS_TO_REDUCTION = 15;
            Settings.LEAVE_CORPSE = false;
            Settings.HAS_WALL = false;
            Settings.SELF_COLLISION = false;
            Settings.SAVE_EVERY_N_GENERATIONS = 200;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.IS_FREE_EVOLUTION = false;
        };
    }

    public static Configurator singleSnakeSettings() {
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
            Settings.IS_FREE_EVOLUTION = false;
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
            Settings.IS_FREE_EVOLUTION = true;
        };
    }

    public static Configurator freeEvolutionRandomSettings() {
        return () -> {
            Settings.SHOW_LEGEND = false;
            Settings.GRID_COLUMN_PIXELS = 1920;
            Settings.PIXELS_PER_SQUARE = 20;
            Settings.NUM_OF_PLAYERS = 25;
            Settings.CHANCE_MUTATE_WEIGHT = Util.randomDouble(0.5d, 5.0d);
            Settings.CHANCE_HARD_MUTATE_WEIGHT = Util.randomDouble(0.05d, 2.0d);
            Settings.CHANCE_SWITCH_CONNECTION_ENABLED = Util.randomDouble(0.05d, 3.0d);
            Settings.CHANCE_ADD_NODE = Util.randomDouble(0.01d, 1.0d);
            Settings.CHANCE_ADD_CONNECTION = Util.randomDouble(0.01d, 1.0d);
            Settings.MAX_NUM_OF_FOOD = Util.randomInt(100, 500);
            Settings.LEAVE_CORPSE = Util.isRandomChanceTrue(0.5);
            Settings.HAS_WALL = Util.isRandomChanceTrue(0.5);
            Settings.SELF_COLLISION = Util.isRandomChanceTrue(0.5);
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.NUM_FOOD_PER_TURN = Util.randomInt(1, 20);
            Settings.OFFSPRING_THRESHOLD = Util.randomDouble(0.05d, 0.5d);
            Settings.MAX_NUM_OF_MOVES = 10000;
            Settings.IS_FREE_EVOLUTION = true;
        };
    }

    public static Configurator multiplayerRandomSettings() {
        var pixelsPerSquare = Util.randomInt(1, 6) * 10;
        return () -> {
            Settings.NUM_OF_GENERATIONS = 1000;
            Settings.NUM_OF_PLAYERS = Util.randomInt(1, 12) * 2;
            Settings.GRID_COLUMN_PIXELS = 1500;
            Settings.GRID_ROW_PIXELS = 1080;
            Settings.PIXELS_PER_SQUARE = pixelsPerSquare;
            Settings.SNAKE_SIGHT = 7;
            Settings.DEATH_PENALTY = Util.randomInt(-100, 0);
            Settings.MAX_NUM_OF_FOOD = Util.randomInt(1, 5000) / pixelsPerSquare;
            Settings.STEPS_TO_REDUCTION = Util.randomInt(10, 100);
            Settings.LEAVE_CORPSE = Util.isRandomChanceTrue(0.5);
            Settings.HAS_WALL = Util.isRandomChanceTrue(0.5);
            Settings.SELF_COLLISION = Util.isRandomChanceTrue(0.5);
            Settings.SAVE_EVERY_N_GENERATIONS = 200;
            Settings.SNAKE_SIGHT_TYPE = SnakeSightType.RAYS;
            Settings.IS_FREE_EVOLUTION = false;
            Settings.MAX_NUM_OF_MOVES_VIDEO = 500;
        };
    }
}
