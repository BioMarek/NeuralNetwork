package NEAT.Evolution;

import Games.Game;
import Interfaces.EvolutionEngine;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static Utils.Util.repeat;

/**
 * The class holds all genes of population.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenePool implements EvolutionEngine {
    public Function<Double, Double> hiddenLayerActivationFunc;
    public Function<Double, Double> outputLayerActivationFunc;
    private List<Species> speciesList = new ArrayList<>();

    protected int maxNeurons;
    protected int maxNumberOfMoves; // to stop AI moving in cycles
    protected int numOfTrials; // how many times NeuralNetwork plays the game
    protected double chanceToMutateWeight; // chance that weight will be mutated
    protected double chanceToHardMutateWight; // chance to assign new value to weight when it is being mutated, small change otherwise
    protected double chanceToAddNode;
    protected double chanceToAddConnection;
    protected double networksToKeep; // portion of top scoring networks that are copied into next generation
    protected double networksToMutate; // portion of top scoring networks copies that are mutated and copied into next generation
    public double speciesReduction; // TODO refactor so it is accessible from test
    protected int speciesMinimalReduction; // Minimal amount by which the size of underperforming species will be reduced
    protected int protectedAge; // Age when species stops being protected and its size can be reevaluated
    protected int frequencyOfSpeciation;
    protected int networksGenerated;
    protected int speciesNames;
    protected Game game;
    protected boolean verbose;

    @Override
    public void calculateEvolution(int numOfGenerations) {
        for (int i = 0; i < numOfGenerations; i++) {
            System.out.printf("\nGeneration %d %s\n", i, "-".repeat(200));
            if (i % frequencyOfSpeciation == 0 && i > 0)
                createSpecies();
            resetScores();
            makeNextGeneration();
            resizeSpecies();
            removeDeadSpecies();
            printSpecies();
        }
    }

    @Override
    public void makeNextGeneration() {
        for (Species species : speciesList) {
            species.calculateScores();  // rename
            species.calculateAverage();
        }
        speciesList.sort(Collections.reverseOrder());

        if (verbose)
            printScores();

        for (Species species : speciesList) {
            species.mutateSpecies();
            species.increaseAge();
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

        speciesList.add(new Species(this, speciesGenotypes, speciesNames++));
    }

    /**
     * Adjusts sizes of {@link Species} based on their performance. Size of the best {@link Species} are increased and
     * the worst reduced. Young species are protected, that is cannot be resized in order to give them time to optimize
     * new mutations. How big adjustions are made is specified in {@link GenePool}.
     */
    public void resizeSpecies() {
        List<Species> oldSpeciesList = speciesList.stream()
                .filter(species -> species.age > protectedAge)
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
        speciesList.forEach(species -> {
            System.out.printf("%-3d: ", species.name);
            species.genotypes.forEach(Genotype::printScores);
            System.out.println();
        });
    }

    public void printSpecies() {
        System.out.print("Species: ");
        for (Species species : speciesList) {
            System.out.printf("\"%d\": %.2f size: %d, age: %d | ", species.name, species.average / numOfTrials, species.getSize(), species.age);
        }
        System.out.println();
    }

    public List<Species> getSpecies() {
        return speciesList;
    }

    public static class GenePoolBuilder {
        private final int inputs;
        private final int outputs;
        private final Game game;
        private final Function<Double, Double> hiddenLayerActivationFunc;

        // default values
        private Function<Double, Double> outputLayerActivationFunc;
        private int totalNumOfGenotypes = 100;
        private int maxNeurons = 1000;
        private boolean verbose = true;
        private int maxNumberOfMoves = 500;
        private int numOfTrials = 10;
        private double chanceToMutateWeight = 0.8d;
        private double chanceToHardMutateWight = 0.1d;
        private double chanceToAddNode = 0.03d;
        private double chanceToAddConnection = 0.03d;
        private double networksToKeep = 0.3d;
        private double speciesReduction = 0.1d;
        private int speciesMinimalReduction = 2;
        private int protectedAge = 15;
        private int frequencyOfSpeciation = 10;
        private int networksGenerated = 0;
        private int speciesNames = 0;

        public GenePoolBuilder(int inputs, int outputs, Function<Double, Double> hiddenLayerActivationFunc, Game game) {
            this.inputs = inputs;
            this.outputs = outputs;
            this.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
            this.game = game;

            this.outputLayerActivationFunc = hiddenLayerActivationFunc;
        }

        public GenePoolBuilder setOutputLayerActivationFunc(Function<Double, Double> outputLayerActivationFunc) {
            this.outputLayerActivationFunc = outputLayerActivationFunc;
            return this;
        }

        public GenePoolBuilder setTotalNumOfGenotypes(int totalNumOfGenotypes) {
            this.totalNumOfGenotypes = totalNumOfGenotypes;
            return this;
        }

        public GenePoolBuilder setMaxNeurons(int maxNeurons) {
            this.maxNeurons = maxNeurons;
            return this;
        }

        public GenePoolBuilder setVerbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        public GenePoolBuilder setMaxNumberOfMoves(int maxNumberOfMoves) {
            this.maxNumberOfMoves = maxNumberOfMoves;
            return this;
        }

        public GenePoolBuilder setNumOfTrials(int numOfTrials) {
            this.numOfTrials = numOfTrials;
            return this;
        }

        public GenePoolBuilder setChanceToMutateWeight(double chanceToMutateWeight) {
            this.chanceToMutateWeight = chanceToMutateWeight;
            return this;
        }

        public GenePoolBuilder setChanceToHardMutateWight(double chanceToHardMutateWight) {
            this.chanceToHardMutateWight = chanceToHardMutateWight;
            return this;
        }

        public GenePoolBuilder setChanceToAddNode(double chanceToSplitConnection) {
            this.chanceToAddNode = chanceToSplitConnection;
            return this;
        }

        public GenePoolBuilder setChanceToAddConnection(double chanceToAddConnection) {
            this.chanceToAddConnection = chanceToAddConnection;
            return this;
        }

        public GenePoolBuilder setNetworksToKeep(double networksToKeep) {
            this.networksToKeep = networksToKeep;
            return this;
        }

        public GenePoolBuilder setSpeciesReduction(double speciesReduction) {
            this.speciesReduction = speciesReduction;
            return this;
        }

        public GenePoolBuilder setSpeciesMinimalReduction(int speciesMinimalReduction) {
            this.speciesMinimalReduction = speciesMinimalReduction;
            return this;
        }

        public GenePoolBuilder setProtectedAge(int protectedAge) {
            this.protectedAge = protectedAge;
            return this;
        }

        public GenePoolBuilder setAgeToMakeNewSpecies(int ageToMakeNewSpecies) {
            this.frequencyOfSpeciation = ageToMakeNewSpecies;
            return this;
        }

        public GenePoolBuilder setNetworksGenerated(int networksGenerated) {
            this.networksGenerated = networksGenerated;
            return this;
        }

        public GenePool build() {
            GenePool genePool = new GenePool();
            genePool.game = game;
            genePool.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
            genePool.outputLayerActivationFunc = outputLayerActivationFunc;
            genePool.maxNeurons = maxNeurons;
            genePool.verbose = verbose;
            genePool.maxNumberOfMoves = maxNumberOfMoves;
            genePool.numOfTrials = numOfTrials;
            genePool.chanceToMutateWeight = chanceToMutateWeight;
            genePool.chanceToHardMutateWight = chanceToHardMutateWight;
            genePool.chanceToAddNode = chanceToAddNode;
            genePool.chanceToAddConnection = chanceToAddConnection;
            genePool.networksToKeep = networksToKeep;
            genePool.speciesReduction = speciesReduction;
            genePool.speciesMinimalReduction = speciesMinimalReduction;
            genePool.protectedAge = protectedAge;
            genePool.networksGenerated = networksGenerated;
            genePool.speciesNames = speciesNames;
            genePool.frequencyOfSpeciation = frequencyOfSpeciation;

            genePool.speciesList = new ArrayList<>();
            List<Genotype> genotypes = new ArrayList<>();
            repeat.accept(totalNumOfGenotypes, () -> genotypes.add(new Genotype(genePool, inputs, outputs)));
            Species species = new Species(genePool, genotypes, genePool.speciesNames++);
            genePool.speciesList.add(species);

            return genePool;
        }
    }
}
