package utils;

import java.util.function.Function;

public class Settings {
    public static boolean HAS_WALL = true;
    public static int NUM_OF_GENERATIONS = 300; // 300
    public static int TOTAL_NUM_OF_GENOTYPES = 100;
    public static int MAX_NEURONS = 1000;
    public static Function<Double, Double> HIDDEN_LAYER_ACTIVATION_FUNC = Util.activationFunctionHyperbolicTangent();
    public static Function<Double, Double> OUTPUT_LAYER_ACTIVATION_FUNC = Util.activationFunctionHyperbolicTangent();
    public static boolean VERBOSE = true;
    public static int MAX_NUM_OF_MOVES = 500;  // to stop AI moving in cycles
    public static int MAX_NUM_OF_MOVES_VIDEO = 2000;  // to stop AI moving in cycles
    public static int NUM_OF_TRIALS = 10; // how many times NeuralNetwork plays the game
    public static double CHANCE_MUTATE_WEIGHT = 0.8d; // chance that weight will be mutated
    public static double CHANCE_HARD_MUTATE_WEIGHT = 0.1d; // chance to assign new value to weight when it is being mutated, small change otherwise
    public static double CHANCE_SWITCH_CONNECTION_ENABLED = 0.2d;
    public static double CHANCE_ADD_NODE = 0.03d;
    public static double CHANCE_ADD_CONNECTION = 0.03d;
    public static double NETWORKS_TO_KEEP = 0.3d; // portion of top scoring networks that are copied into next generation
    public double NETWORKS_TO_MUTATE; // portion of top scoring networks copies that are mutated and copied into next generation
    public static double SPECIES_REDUCTION = 0.1d;
    public static int MIN_SPECIES_REDUCTION = 2; // Minimal amount by which the size of underperforming species will be reduced
    public static int PROTECTED_AGE = 15; // Age when species stops being protected and its size can be reevaluated
    public static int FREQUENCY_OF_SPECIATION = 10;
    public static String SAVE_GAME_PATH = "savedGames/";
    public static boolean SELF_COLLISION = false;

    /**
     * Multiplayer game
     */
    public static int NUM_OF_PLAYERS = 2;
    public static int MAX_NUM_OF_FOOD = 30;
    public static int DEATH_PENALTY = -3; // should be negative or zero
    public static int SNAKE_SIGHT = 7;
    public static boolean LEAVE_CORPSE = true; // leaves food in place of dead snake BodyParts
    public static int STEPS_TO_REDUCTION = 15; // simulates starvation, snakes running in circles will be eliminated

    /**
     * Free evolution
     */
    public static boolean SHOW_LEGEND = false;
    public static int OFFSPRING_COST = 3; // number of bodyparts that are removed when offspring is generated
    public static int OFFSPRING_THRESHOLD = 6;

    /**
     * Graphic
     */
    public static int TIMER_DELAY = 50;
    public static int GRID_COLUMN_PIXELS = 1500;
    public static int GRID_ROW_PIXELS = 1080;
    public static int BACKGROUND_WIDTH = 1920;
    public static int BACKGROUND_HEIGHT = 1080;
    public static int PIXELS_PER_SQUARE = 20;
    public static int CHANGING_SLOW_FRAMES = 15;

    /**
     * Settings for mp4 generation
     */
    public static int VIDEO_FPS = 20; // frames per second for mp4
    public static String VIDEO_BASE_PATH = "./movies/"; // where to save generated videos
    public static int VIDEO_REPEAT_LAST_FRAME = 0; // how many times the last image should be repeated in video, 30 is one second
    public static int SAVE_EVERY_N_GENERATIONS = 10000;


    public static void multiplayerSettings() {
        NUM_OF_PLAYERS = 10;
        GRID_COLUMN_PIXELS = 1500;
        GRID_ROW_PIXELS = 1080;
        PIXELS_PER_SQUARE = 20;
        SNAKE_SIGHT = 10;
        DEATH_PENALTY = -3;
        MAX_NUM_OF_FOOD = 150;
        LEAVE_CORPSE = false;
        HAS_WALL = true;
        SELF_COLLISION = false;
        SAVE_EVERY_N_GENERATIONS = 100;
    }

    public static void introSettings() {
        SHOW_LEGEND = false;
        NUM_OF_PLAYERS = 10;
        GRID_COLUMN_PIXELS = 1920;
        GRID_ROW_PIXELS = 1080;
        PIXELS_PER_SQUARE = 40;
        SNAKE_SIGHT = 10;
        DEATH_PENALTY = -3;
        MAX_NUM_OF_FOOD = 10;
        LEAVE_CORPSE = false;
        HAS_WALL = true;
        SELF_COLLISION = true;
        SAVE_EVERY_N_GENERATIONS = 100;
    }

    public static void freeEvolutionSettings() {
        SHOW_LEGEND = false;
        GRID_COLUMN_PIXELS = 1500;
        PIXELS_PER_SQUARE = 20;
        NUM_OF_PLAYERS = 25;
        CHANCE_MUTATE_WEIGHT = 0.8d;
        CHANCE_HARD_MUTATE_WEIGHT = 0.1d;
        CHANCE_SWITCH_CONNECTION_ENABLED = 0.2d;
        CHANCE_ADD_NODE = 0.03d;
        CHANCE_ADD_CONNECTION = 0.03d;
        OFFSPRING_COST = 3;
        MAX_NUM_OF_FOOD = 300;
        LEAVE_CORPSE = false;
        HAS_WALL = false;
    }

    public static void explanationSettings() {
        GRID_COLUMN_PIXELS = 1920;
        GRID_ROW_PIXELS = 1080;
    }

    public static void singlePlayerGame() {
        NUM_OF_PLAYERS = 1;
        SNAKE_SIGHT = 15;
        DEATH_PENALTY = -1;
        MAX_NUM_OF_FOOD = 1;
        PIXELS_PER_SQUARE = 50;
        STEPS_TO_REDUCTION = 501;
        LEAVE_CORPSE = false;
        HAS_WALL = true;
        SELF_COLLISION = true;
        SAVE_EVERY_N_GENERATIONS = 100;
    }
}
