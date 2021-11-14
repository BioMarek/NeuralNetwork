package NEAT;

import NEAT.Evolution.ConnectionGene;
import NEAT.Evolution.GenePool;
import NEAT.Evolution.NodeGene;
import NEAT.Phenotype.Connection;
import NEAT.Phenotype.NEATNeuron;
import NEAT.Phenotype.Phenotype;
import Utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GenePoolTest {
    private final static int MAX_NEURONS = 1000;
    protected GenePool genePool = new GenePool();

    @BeforeEach
    void init() {
        genePool.initGenePool(2, 2, Util.activationFunctionIdentity(), Util.activationFunctionIdentity());
        genePool.createPhenotypes();
    }

    @Test
    void initGenePool_initialNodeGenesConfigurationIsCorrect() {
        List<NodeGene> nodeGenes = genePool.nodeGenes;
        assertThat(nodeGenes.get(0).name, is(0));
        assertThat(nodeGenes.get(0).type, is(NeuronType.INPUT));
        assertThat(nodeGenes.get(1).name, is(1));
        assertThat(nodeGenes.get(1).type, is(NeuronType.INPUT));
        assertThat(nodeGenes.get(2).name, is(MAX_NEURONS));
        assertThat(nodeGenes.get(2).type, is(NeuronType.OUTPUT));
        assertThat(nodeGenes.get(3).name, is(MAX_NEURONS - 1));
        assertThat(nodeGenes.get(3).type, is(NeuronType.OUTPUT));
    }

    @Test
    void initGenePool_initialConnectionConfigurationIsCorrect() {
        List<ConnectionGene> connectionGenes = genePool.connectionGenes;
        assertThat(connectionGenes.get(0).from.name, is(0));
        assertThat(connectionGenes.get(0).to.name, is(MAX_NEURONS));
        assertThat(connectionGenes.get(0).enabled[0], is(true));
        assertThat(connectionGenes.get(1).from.name, is(0));
        assertThat(connectionGenes.get(1).to.name, is(MAX_NEURONS - 1));
        assertThat(connectionGenes.get(1).enabled[1], is(true));
        assertThat(connectionGenes.get(2).from.name, is(1));
        assertThat(connectionGenes.get(2).to.name, is(MAX_NEURONS));
        assertThat(connectionGenes.get(2).enabled[2], is(true));
        assertThat(connectionGenes.get(3).from.name, is(1));
        assertThat(connectionGenes.get(3).to.name, is(MAX_NEURONS - 1));
        assertThat(connectionGenes.get(3).enabled[3], is(true));
    }

    @Test
    void createPhenotypes_firstPhenotypeFirsNeuronIsCorrect() {
        NEATNeuron neuron = genePool.phenotypes.get(0).inputNeurons.get(0);
        assertThat(neuron.name, is(0));
        assertThat(neuron.bias, is(0.0));
        assertThat(neuron.neuronType, is(NeuronType.INPUT));
        assertThat(neuron.innerPotential, is(0.0));
    }

    @Test
    void createPhenotypes_connectionsAreCorrect() {
        List<Connection> connections = genePool.phenotypes.get(0).connections;
        List<NEATNeuron> neurons = genePool.phenotypes.get(0).neurons;
        assertThat(connections.get(0).from, is(neurons.get(0)));
        assertThat(connections.get(0).to, is(neurons.get(2)));
        assertThat(connections.get(1).from, is(neurons.get(0)));
        assertThat(connections.get(1).to, is(neurons.get(3)));
        assertThat(connections.get(2).from, is(neurons.get(1)));
        assertThat(connections.get(2).to, is(neurons.get(2)));
        assertThat(connections.get(3).from, is(neurons.get(1)));
        assertThat(connections.get(3).to, is(neurons.get(3)));
    }

    @Test
    void evaluate_producesCorrectOutputWhenAllWeightsAreOne() {
        List<Connection> connections = genePool.phenotypes.get(0).connections;
        Phenotype phenotype = genePool.phenotypes.get(0);
        connections.get(0).weight = 1;
        connections.get(1).weight = 1;
        connections.get(2).weight = 1;
        connections.get(3).weight = 1;
        assertThat(phenotype.getOutput(new double[]{1, 1}, Util.activationFunctionIdentity(), Util.activationFunctionIdentity()), is(new double[]{2, 2}));
    }

    @Test
    void evaluate_producesCorrectOutputWhenAllWeightsAreZero() {
        List<Connection> connections = genePool.phenotypes.get(0).connections;
        Phenotype phenotype = genePool.phenotypes.get(0);
        connections.get(0).weight = 0;
        connections.get(1).weight = 0;
        connections.get(2).weight = 0;
        connections.get(3).weight = 0;
        assertThat(phenotype.getOutput(new double[]{1, 1}, Util.activationFunctionIdentity(), Util.activationFunctionIdentity()), is(new double[]{0, 0}));
    }

    @Test
    void evaluate_producesCorrectOutputWhenOneWeightInEachNeuronIsZero() {
        List<Connection> connections = genePool.phenotypes.get(0).connections;
        Phenotype phenotype = genePool.phenotypes.get(0);
        connections.get(0).weight = 1;
        connections.get(1).weight = 0;
        connections.get(2).weight = 1;
        connections.get(3).weight = 0;
        assertThat(phenotype.getOutput(new double[]{1, 1}, Util.activationFunctionIdentity(), Util.activationFunctionIdentity()), is(new double[]{2, 0}));
    }

    @Test
    void evaluate_producesCorrectOutputWhenAllWeightsOfFirstNeuronAreZero() {
        List<Connection> connections = genePool.phenotypes.get(0).connections;
        Phenotype phenotype = genePool.phenotypes.get(0);
        connections.get(0).weight = 0;
        connections.get(1).weight = 0;
        connections.get(2).weight = 1;
        connections.get(3).weight = 1;
        assertThat(phenotype.getOutput(new double[]{1, 1}, Util.activationFunctionIdentity(), Util.activationFunctionIdentity()), is(new double[]{1, 1}));
    }
}
