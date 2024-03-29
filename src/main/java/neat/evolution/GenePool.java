package neat.evolution;

import games.MultiplayerGame;
import games.snake.savegame.SavedGameDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import neat.phenotype.NeuralNetwork;
import utils.Settings;
import visualizations.snakeGraphic.videoGeneration.VideoGenerator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Util.repeat;

/**
 * The class holds all genes of population.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenePool implements EvolutionEngine {
    private final List<Species> speciesList = new ArrayList<>();
    protected int networksGenerated;
    protected int speciesNames;
    private MultiplayerGame multiplayerGame;
    protected int speciesCreated = 1;
    public SavedGameDTO savedGameDTO;

    public GenePool(int inputs, int outputs, MultiplayerGame game) {
        List<Genotype> genotypes = new ArrayList<>();
        repeat.accept(Settings.TOTAL_NUM_OF_GENOTYPES, () -> genotypes.add(new Genotype(this, inputs, outputs)));
        Species species = new Species(genotypes, this.speciesNames++);
        this.speciesList.add(species);
        this.multiplayerGame = game;
    }


    @Override
    public void calculateEvolution() {
        for (int generation = 0; generation < Settings.NUM_OF_GENERATIONS; generation++) {
            System.out.printf("\nGeneration %d %s\n", generation, "-".repeat(200));
            if (generation % Settings.FREQUENCY_OF_SPECIATION == 0 && generation > 0)
                createSpecies();
            resetScores();
            if (generation > 0 && (generation % Settings.SAVE_EVERY_N_GENERATIONS == 0 || generation == Settings.NUM_OF_GENERATIONS - 1)) {
                makeNextGeneration(true);
                var videoGenerator = new VideoGenerator();
                videoGenerator.generateSavedGameVideo(this.savedGameDTO);
//                SaveGameUtil.saveObjectToFile(this.savedGameDTO);
            } else
                makeNextGeneration(false);
            resizeSpecies();
            removeDeadSpecies();
            printSpecies();
        }
    }

    @Override
    public void makeNextGeneration(boolean saveGame) {
        List<List<Genotype>> allGenotypes = divideGenotypes(shuffleGenotypesFromSpecies());

        for (var players : allGenotypes) {
            var phenotypes = players.stream()
                    .map(player -> (NeuralNetwork) player.createPhenotype())
                    .toList();

            for (int i = 0; i < Settings.NUM_OF_TRIALS; i++) {
                if (saveGame) {
                    savedGameDTO = this.multiplayerGame.saveSnakeMoves(phenotypes);
                    return;
                }
                var score = this.multiplayerGame.play(phenotypes);
                updateMultiplayerScore(score, players);
                this.multiplayerGame.reset();
            }
        }

        for (Species species : speciesList) {
            species.genotypes.sort(Collections.reverseOrder());
            species.calculateAverage();
        }
        speciesList.sort(Collections.reverseOrder());
        printScores();
        speciesList.forEach(Species::mutateAndAge);
    }

    public List<List<Genotype>> divideGenotypes(List<Genotype> allGenotypes) {
        List<List<Genotype>> result = new ArrayList<>();
        List<Genotype> listOfPlayers = new ArrayList<>();

        for (int i = 0; i < allGenotypes.size(); i++) {
            if (i % Settings.NUM_OF_PLAYERS != 0 || i == 0) {
                listOfPlayers.add(allGenotypes.get(i));
            } else {
                result.add(listOfPlayers);
                listOfPlayers = new ArrayList<>();
                listOfPlayers.add(allGenotypes.get(i));
            }
        }
        result.add(listOfPlayers);

        // handles case where NUM_OF_PLAYERS doesn't divide TOTAL_NUM_OF_GENOTYPES
        // TODO check that scores are now calculated correctly
        var lastListOfPlayers = result.get(result.size() - 1);
        var numOfPlayersMissing = Settings.NUM_OF_PLAYERS - lastListOfPlayers.size();
        if (numOfPlayersMissing > 0) {
            for (int i = 0; i < numOfPlayersMissing; i++) {
                lastListOfPlayers.add(listOfPlayers.get(i));
            }
        }

        return result;
    }

    /**
     * Takes all {@link Genotype} from all {@link Species} and shuffles them
     *
     * @return list of all genotypes shuffled
     */
    public List<Genotype> shuffleGenotypesFromSpecies() {
        List<Genotype> allGenotypes = new ArrayList<>();
        speciesList.forEach(species -> allGenotypes.addAll(species.genotypes));
        Collections.shuffle(allGenotypes);
        return allGenotypes;
    }

    public void updateMultiplayerScore(int[] score, List<Genotype> players) {
        for (var i = 0; i < players.size(); i++) {
            players.get(i).score += score[i];
        }
    }

    /**
     * Creates new {@link Species} by reducing size of existing species and filling space with new species
     * {@link Genotype}. The new {@link Species} mutation adds new random node.
     */
    public void createSpecies() {
        List<Genotype> speciesGenotypes = new ArrayList<>();
        Genotype genotypeToSpeciate = speciesList.get(0).genotypes.get(0);
        genotypeToSpeciate.addNode();

        int emptyPlaces = reduceSpeciesSizesUniformly();
        removeDeadSpecies();
        repeat.accept(emptyPlaces, () -> speciesGenotypes.add(genotypeToSpeciate.copy()));

        speciesList.add(new Species(speciesGenotypes, speciesNames++));
        speciesCreated++;
    }

    /**
     * Adjusts sizes of {@link Species} based on their performance. Size of the best {@link Species} are increased and
     * the worst reduced. Young species are protected, that is cannot be resized in order to give them time to optimize
     * new mutations. How big adjustions are made is specified in {@link GenePool}.
     */
    public void resizeSpecies() {
        List<Species> oldSpeciesList = speciesList.stream()
                .filter(species -> species.age > Settings.PROTECTED_AGE)
                .collect(Collectors.toList());

        if (oldSpeciesList.size() >= 2) {
            System.out.print("Old sizes: ");
            speciesList.forEach((species) -> System.out.print(species.getSize() + " "));
            System.out.println();

            oldSpeciesList.sort(Collections.reverseOrder());
            for (int i = 0; i < oldSpeciesList.size() / 2; i++) {
                int change = oldSpeciesList.get(oldSpeciesList.size() - i - 1).reduceSize();
                oldSpeciesList.get(i).increaseSize(change);
            }

            System.out.print("New sizes: ");
            speciesList.forEach((species) -> System.out.print(species.getSize() + " "));
            System.out.println();
        }
    }

    /**
     * Reduces sizes of all species by amount set in {@link GenePool}. Used for example when room for new species has to
     * be done. Due to rounding errors number of actually removed {@link Genotype} is returned.
     *
     * @return Number of removed {@link Genotype}.
     */
    public int reduceSpeciesSizesUniformly() {
        return speciesList.stream()
                .map(Species::reduceSize)
                .reduce(0, Integer::sum);
    }

    /**
     * Deletes all species with size 0.
     */
    public void removeDeadSpecies() {
        speciesList.removeIf(Species::isExtinct);
    }

    /**
     * Creates *.csv file with information with evolution of species.
     *
     * @param filePath      where should file be created
     * @param stringToWrite string containing information about species evolution
     */
    public void saveSpeciesCsvFile(String filePath, String stringToWrite) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Path.of(filePath));
            writer.write(stringToWrite);
            writer.close();
        } catch (IOException e) {
            System.out.println("File could not be created\n" + e);
        }
    }

    /**
     * Adds line stating number of {@link  Species} and number of generations to {@link StringBuilder} csvString.
     * This line is added as first line of the *.csv file.
     *
     * @param csvString {@link StringBuilder} to which line should be added
     */
    public void addSpeciesMetadata(StringBuilder csvString, int numOfGenerations) {
        csvString.insert(0, speciesCreated + "," + numOfGenerations + "\n");
    }

    /**
     * Adds line specifying species names and counts in current generation to {@link StringBuilder} csvString.
     *
     * @param csvString {@link StringBuilder} to which line should be added
     */
    public void addSpeciesCsvString(StringBuilder csvString) {
        for (Species species : speciesList) {
            csvString.append(species.name).append(":").append(species.getSize()).append(",");
        }
        csvString.append("\n");
    }

    /**
     * Resets game scores in all {@link Genotype} in all {@link Species}.
     */
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

    public void printSpecies() {
        System.out.print("Species: ");
        for (Species species : speciesList) {
            System.out.printf("\"%d\": %.2f size: %d, age: %d | ", species.name, species.average / Settings.NUM_OF_TRIALS, species.getSize(), species.age);
        }
        System.out.println();
    }

    public List<Species> getSpecies() {
        return speciesList;
    }
}
