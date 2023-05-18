package neat.evolution;

import games.MultiplayerGame;
import games.snake.savegame.SavedGameDTO;
import utils.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static utils.Util.repeat;

public class FreeEvolution implements EvolutionEngine {
    private final List<Species> speciesList = new ArrayList<>();
    protected int networksGenerated;
    protected int speciesNames;
    private MultiplayerGame multiplayerGame;
    protected int speciesCreated = 1;
    public SavedGameDTO savedGameDTO;

    public FreeEvolution(int inputs, int outputs, MultiplayerGame game) {
        List<Genotype> genotypes = new ArrayList<>();
        repeat.accept(Settings.TOTAL_NUM_OF_GENOTYPES, () -> genotypes.add(new Genotype(this, inputs, outputs)));
        Species species = new Species(genotypes, this.speciesNames++);
        this.speciesList.add(species);
        this.multiplayerGame = game;
    }

    @Override
    public void calculateEvolution(int numOfGenerations) {

    }

    @Override
    public void makeNextGeneration(boolean saveGame) {

    }

    @Override
    public void resetScores() {

    }

    @Override
    public void printScores() {

    }

    @Override
    public int networksGeneratedIncrease() {
        return 0;
    }
}
