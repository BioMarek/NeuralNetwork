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


    public static class GenePoolBuilder {
        private final int inputs;
        private final int outputs;
        private final Game game;
        private final Function<Double, Double> hiddenLayerActivationFunc;
        private Function<Double, Double> outputLayerActivationFunc;
        private int totalNumOfGenotypes = 100;
        private int maxNeurons = 1000;
        private boolean verbose = true;

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

        public GenePool build() {
            GenePool genePool = new GenePool();
            genePool.game = this.game;
            genePool.inputs = this.inputs;
            genePool.outputs = this.outputs;
            genePool.hiddenLayerActivationFunc = this.hiddenLayerActivationFunc;
            genePool.outputLayerActivationFunc = this.outputLayerActivationFunc;
            genePool.totalNumOfGenotypes = this.totalNumOfGenotypes;
            genePool.verbose = this.verbose;

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
