package NEAT.Evolution;

import Games.Game;
import Interfaces.EvolutionEngine;
import NEAT.NeuronType;
import NEAT.Phenotype.Connection;
import Utils.Pair;
import Utils.Util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The class holds all genes of population.
 */
public class GenePool implements EvolutionEngine {
    private int totalNumOfGenotypes;
    private int inputs;
    private int outputs;
    private int neuronNames = 0;
    private Function<Double, Double> hiddenLayerActivationFunc;
    private Function<Double, Double> outputLayerActivationFunc;
    private final Set<Integer> nodeGenes = new HashSet<>();
    private final Set<Pair<Integer>> connections = new HashSet<>();
    private List<Species> speciesList = new ArrayList<>();

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
            if (i % frequencyOfSpeciation == 0 && i > 0)
                createSpecies();
            resetScores();
            makeNextGeneration();
            resizeSpecies();
            removeDeadSpecies();
            printSpecies();
            System.out.println("------------------------------------------------------------------------------------");
        }
    }

    @Override
    public void makeNextGeneration() {
        for (Species species : speciesList) {
            species.calculateScores();  // rename
            species.calculateAverage();
            species.mutateSpecies();
            species.increaseAge();
        }
        speciesList.sort(Collections.reverseOrder());

        if (verbose)
            printScores();
    }

    public void createSpecies() {
        List<Genotype> speciesGenotypes = new ArrayList<>();
        Genotype genotypeToSpeciate = speciesList.get(0).genotypes.get(0);
        genotypeToSpeciate.addNode();

        int emptyPlaces = reduceSpeciesSizesUniformly();
        removeDeadSpecies();
        Util.repeat.accept(emptyPlaces, () -> speciesGenotypes.add(genotypeToSpeciate.copy()));

        speciesList.add(new Species(this, speciesGenotypes, speciesNames++));
    }

    /**
     * Adjusts sizes of species based on their performance. Size of the best species are increased and the worst
     * reduced.
     */
    public void resizeSpecies() {
        List<Species> oldSpecies = speciesList.stream()
                .filter(species -> species.age > protectedAge)
                .collect(Collectors.toList());

        if (oldSpecies.size() >= 2) {
            System.out.print("Old sizes: ");
            speciesList.forEach((species) -> System.out.print(species.getSize() + " "));
            System.out.println("");

            oldSpecies.sort(Collections.reverseOrder());
            for (int i = 0; i < oldSpecies.size() / 2; i++) {
                int change = oldSpecies.get(oldSpecies.size() - i - 1).reduceSize();
                oldSpecies.get(i).increaseSize(change);
            }

            System.out.print("New sizes: ");
            speciesList.forEach((species) -> System.out.print(species.getSize() + " "));
            System.out.println("");
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
        speciesList.removeIf(species -> species.getSize() == 0);
    }

    /**
     * Resets game scores in all {@link Genotype} in all {@link Species}.
     */
    @Override
    public void resetScores() {
        for (Species species : speciesList) {
            species.average = 0.0;
            species.genotypes.forEach((genotype -> genotype.score = 0));
        }
    }

    @Override
    public void printScores() {
        for (Species species : speciesList) {
            System.out.println(species.name);
            species.genotypes.forEach(genotype -> System.out.print(genotype.name + ": " + genotype.score + ", "));
        }
        System.out.println();
    }


    public void printSpecies() {
        System.out.print("Species: ");
        for (Species species : speciesList) {
            System.out.printf("\"%d\": %.4f, size: %d, age: %d | ", species.name, species.average, species.getSize(), species.age);
        }
        System.out.println();
    }

    public void putConnectionGeneIntoGenePool(int from, int to) {
        connections.add(new Pair<>(from, to));
    }

    public void putConnectionGeneIntoGenePool(ConnectionGene connectionGene) {
        putConnectionGeneIntoGenePool(connectionGene.from.name, connectionGene.to.name);
    }

    /**
     * Returns name which should node of split {@link Connection} have. If there is a pair of {@link Connection} x -> z
     * and z -> y it means that {@link Connection} x -> y was already split by some other genome. If genome wants to
     * split its x -> y {@link Connection} nodeNameOfSplitConnection will either add new node name into {@link GenePool}
     * or returns z such as {@link Connection} x -> z and z -> y exists.
     *
     * @param connectionGene to split
     * @return name which split connection should have
     */
    public int nodeNameOfSplitConnection(ConnectionGene connectionGene) {
        List<Integer> from = new ArrayList<>();
        List<Integer> to = new ArrayList<>();

        for (Pair<Integer> connection : connections) {
            if (connectionGene.from.name == connection.getFirst())
                from.add(connection.getSecond());
            if (connectionGene.to.name == connection.getSecond())
                to.add(connection.getFirst());
        }
        from.retainAll(to);

        if (from.size() > 0)
            return from.get(0);
        return neuronNames++;
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
        private double chanceToMutateWeight = 0.8;
        private double chanceToHardMutateWight = 0.1;
        private double chanceToSplitConnection = 0.03;
        private double chanceToAddConnection = 0.03;
        private double networksToKeep = 0.3;
        private double speciesReduction = 0.1;
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
            this.chanceToSplitConnection = chanceToSplitConnection;
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

        public GenePoolBuilder setFrequencyOfSpeciation(int frequencyOfSpeciation) {
            this.frequencyOfSpeciation = frequencyOfSpeciation;
            return this;
        }

        public GenePoolBuilder setNetworksGenerated(int networksGenerated) {
            this.networksGenerated = networksGenerated;
            return this;
        }

        public GenePool build() {
            GenePool genePool = new GenePool();
            genePool.game = game;
            genePool.inputs = inputs;
            genePool.outputs = outputs;
            genePool.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
            genePool.outputLayerActivationFunc = outputLayerActivationFunc;
            genePool.totalNumOfGenotypes = totalNumOfGenotypes;
            genePool.verbose = verbose;
            genePool.maxNumberOfMoves = maxNumberOfMoves;
            genePool.numOfTrials = numOfTrials;
            genePool.chanceToMutateWeight = chanceToMutateWeight;
            genePool.chanceToHardMutateWight = chanceToHardMutateWight;
            genePool.chanceToAddNode = chanceToSplitConnection;
            genePool.networksToKeep = networksToKeep;
            genePool.speciesReduction = speciesReduction;
            genePool.speciesMinimalReduction = speciesMinimalReduction;
            genePool.protectedAge = protectedAge;
            genePool.networksGenerated = networksGenerated;
            genePool.speciesNames = speciesNames;
            genePool.frequencyOfSpeciation = frequencyOfSpeciation;

            genePool.speciesList = new ArrayList<>();

            Genotype genotype = initGenotype(genePool);
            List<Genotype> genotypes = new ArrayList<>();
            for (int i = 0; i < totalNumOfGenotypes; i++) {
                genotypes.add(genotype.copy());
            }
            Species species = new Species(genePool, genotypes, genePool.speciesNames++);
            genePool.speciesList.add(species);

            return genePool;
        }

        /**
         * Creates first genotype for particular number of inputs and outputs.
         *
         * @param genePool contains constants and settings.
         */
        public Genotype initGenotype(GenePool genePool) {
            List<NodeGene> inputNodes = new ArrayList<>();
            List<NodeGene> outputNodes = new ArrayList<>();
            List<ConnectionGene> connectionGenes = new ArrayList<>();

            for (int i = 0; i < inputs; i++) {
                NodeGene nodeGene = new NodeGene(NeuronType.INPUT, genePool.neuronNames++);
                inputNodes.add(nodeGene);
            }
            for (int i = 0; i < outputs; i++) {
                NodeGene nodeGene = new NodeGene(NeuronType.OUTPUT, maxNeurons--);
                outputNodes.add(nodeGene);
            }
            for (NodeGene input : inputNodes) {
                for (NodeGene output : outputNodes) {
                    genePool.putConnectionGeneIntoGenePool(input.name, output.name);
                    connectionGenes.add(new ConnectionGene(input, output, Util.randomDouble(), true));
                }
            }
            inputNodes.addAll(outputNodes);

            Collections.sort(inputNodes);
            Collections.sort(connectionGenes);
            Genotype genotype = new Genotype(genePool, inputNodes, connectionGenes, hiddenLayerActivationFunc, outputLayerActivationFunc);
            genotype.name = Integer.toString(networksGenerated++);
            return genotype;
        }
    }
}
