package neat.evolution;

public interface EvolutionEngine {
    void calculateEvolution();

    void makeNextGeneration(boolean saveGame);

    void resetScores();

    void printScores();

    int networksGeneratedIncrease();
}
