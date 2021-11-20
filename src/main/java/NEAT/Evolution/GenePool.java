package NEAT.Evolution;

import Games.Game;
import NEAT.NeuronType;
import Utils.Pair;
import Utils.Util;

import java.util.*;
import java.util.function.Function;

/**
 * The class holds all genes of population.
 */
public class GenePool {
    private int totalNumOfGenotypes = 100;
    public int neuronNames = 0;
    private int inputs;
    private int outputs;
    public Function<Double, Double> hiddenLayerActivationFunc;
    public Function<Double, Double> outputLayerActivationFunc;
    public Set<Integer> nodeGenes = new HashSet<>();
    public Set<Pair<Integer>> connections = new HashSet<>();
    public List<Genotype> genotypes = new ArrayList<>();
    protected Game game;
    protected boolean verbose;
    protected int maxNumberOfMoves; // to stop AI moving in cycles

    public void putConnectionGeneIntoGenePool(int from, int to) {
        connections.add(new Pair<>(from, to));
    }

    public void putConnectionGeneIntoGenePool(ConnectionGene connectionGene) {
        connections.add(new Pair<>(connectionGene.from.name, connectionGene.to.name));
    }

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
        else return -1;
    }

    public static class GenePoolBuilder {
        private final int inputs;
        private final int outputs;
        private final Game game;
        private final Function<Double, Double> hiddenLayerActivationFunc;
        private Function<Double, Double> outputLayerActivationFunc;
        private int totalNumOfGenotypes = 100;
        private int maxNeurons = 1000;
        private boolean verbose = true;
        private int maxNumberOfMoves = 500;

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

            initGenotype(genePool);
            for (int i = 0; i < totalNumOfGenotypes - 1; i++) {
                genePool.genotypes.add(genePool.genotypes.get(0).copy());
            }

            return genePool;
        }

        /**
         * Creates first genotype for particular number of inputs and outputs.
         *
         * @param genePool contains constants and settings.
         */
        public void initGenotype(GenePool genePool) {
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
            genePool.genotypes.add(new Genotype(genePool, inputNodes, connectionGenes, hiddenLayerActivationFunc, outputLayerActivationFunc));
        }
    }
}
