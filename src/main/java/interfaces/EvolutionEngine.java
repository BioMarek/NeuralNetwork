package interfaces;

public interface EvolutionEngine {
    void calculateEvolution(int numOfGenerations);

    void makeNextGeneration();

    void resetScores();

    void printScores();
}
