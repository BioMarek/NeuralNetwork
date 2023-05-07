package neat.evolution;

public interface EvolutionEngine {
    void calculateEvolution(int numOfGenerations);

    void makeNextGeneration(boolean saveGame);

    void resetScores();

    void printScores();
}
