package utils;

public class Settings {
    public static int GRID_SIZE = 20;
    public static boolean HAS_WALL = true;
    public static int NUM_OF_GENERATIONS = 300; // 300
    public static int TOTAL_NUM_OF_GENOTYPES = 100;
    public static int MAX_NEURONS = 1000;
    public static boolean VERBOSE = true;
    public static int MAX_NUM_OF_MOVES = 500;  // to stop AI moving in cycles
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

    // multiplayer game
    public static int NUM_OF_PLAYERS = 2;
    public static int MAX_NUM_OF_FOOD = 30;
    public static int DEATH_PENALTY = -3; // should be negative
    public static int SNAKE_SIGHT = 7;
    public static boolean LEAVE_CORPSE = true;

    // graphic
    public static int TIMER_DELAY = 250;
    public static int BACKGROUND_WIDTH = 1000;
    public static int BACKGROUNG_HEIGHT = 1000;

    public static void multiplayerSettings() {
        NUM_OF_PLAYERS = 10;
        GRID_SIZE = 50;
    }
}
