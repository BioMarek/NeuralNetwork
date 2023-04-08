package utils;

public class Settings {
    public static int gridSize = 20;
    public static boolean hasWalls = true;
    public static int numOfGenerations = 300;
    public static int totalNumOfGenotypes = 100;
    public static int maxNeurons = 1000;
    public static boolean verbose = true;
    public static int maxNumberOfMoves = 500;  // to stop AI moving in cycles
    public static int numOfTrials = 10; // how many times NeuralNetwork plays the game
    public static double chanceToMutateWeight = 0.8d; // chance that weight will be mutated
    public static double chanceToHardMutateWight = 0.1d; // chance to assign new value to weight when it is being mutated, small change otherwise
    public static double chanceToSwitchConnectionEnabled = 0.2d;
    public static double chanceToAddNode = 0.03d;
    public static double chanceToAddConnection = 0.03d;
    public static double networksToKeep = 0.3d; // portion of top scoring networks that are copied into next generation
    public double networksToMutate; // portion of top scoring networks copies that are mutated and copied into next generation
    public static double speciesReduction = 0.1d;
    public static int speciesMinimalReduction = 2; // Minimal amount by which the size of underperforming species will be reduced
    public static int protectedAge = 15; // Age when species stops being protected and its size can be reevaluated
    public static int frequencyOfSpeciation = 10;

    // multiplayer game
    public static int numOfPlayers = 2;
    public static int numOfApples = 10;
    public static int deathPenalty = -10;

    // graphic
    public static int timerDelay = 500;
    public static int backgroundWidth = 1000;
    public static int backgroundHeight = 1000;
    public static int alpha = 255;

    public static void multiplayerSettings() {
        numOfPlayers = 10;
        gridSize = 50;
    }
}
