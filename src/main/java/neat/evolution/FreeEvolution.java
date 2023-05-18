package neat.evolution;

import games.MultiplayerGame;
import games.snake.savegame.SavedGameDTO;
import utils.Settings;

import java.util.ArrayList;
import java.util.List;

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
        repeat.accept(20, () -> genotypes.add(new Genotype(this, inputs, outputs)));
        Species species = new Species(genotypes, this.speciesNames++);
        this.speciesList.add(species);
        this.multiplayerGame = game;
    }

    @Override
    public void calculateEvolution() {
        for (int generation = 0; generation < Settings.NUM_OF_GENERATIONS; generation++) {
            System.out.printf("\nGeneration %d %s\n", generation, "-".repeat(200));
            resetScores();
            makeNextGeneration(false);

            removeDeadSpecies();
            printSpecies();
        }
        makeNextGeneration(true);
    }

    @Override
    public void makeNextGeneration(boolean saveGame) {

    }

    @Override
    public void resetScores() {
        speciesList.forEach(species -> {
            species.average = 0.0d;
            species.genotypes.forEach(Genotype::resetScore);
        });
    }

    @Override
    public void printScores() {
        if (Settings.VERBOSE) {
            speciesList.forEach(species -> {
                System.out.printf("%-3d: ", species.name);
                species.genotypes.forEach(Genotype::printScores);
                System.out.println();
            });
        }
    }

    @Override
    public int networksGeneratedIncrease() {
        return networksGenerated++;
    }


    public void removeDeadSpecies() {
        speciesList.removeIf(Species::isExtinct);
    }

    public void printSpecies() {
        System.out.print("Species: ");
        for (Species species : speciesList) {
            System.out.printf("\"%d\": %.2f size: %d, age: %d | ", species.name, species.average / Settings.NUM_OF_TRIALS, species.getSize(), species.age);
        }
        System.out.println();
    }
}
