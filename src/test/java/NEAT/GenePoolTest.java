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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GenePoolTest {
    private final static int MAX_NEURONS = 1000;
    private final static int NUM_OF_GENOTYPES = 100;
    protected GenePool genePool = new GenePool();

    @BeforeEach
    void init() {
        genePool.initGenePool(2, 2, Util.activationFunctionIdentity(), Util.activationFunctionIdentity());
        genePool.createPhenotypes();
    }

    @Test
    void initGenePool_initialNodeGenesConfigurationIsCorrect() {
        // PREPARE
        List<NodeGene> nodeGenes = genePool.nodeGenes;
        boolean[] trueArray = Util.booleanArray(NUM_OF_GENOTYPES, true);

        // VERIFY
        assertThat(nodeGenes.get(0).name, is(0));
        assertThat(nodeGenes.get(0).type, is(NeuronType.INPUT));
        assertThat(nodeGenes.get(0).enabled, is(trueArray));
        assertThat(nodeGenes.get(1).name, is(1));
        assertThat(nodeGenes.get(1).type, is(NeuronType.INPUT));
        assertThat(nodeGenes.get(1).enabled, is(trueArray));
        assertThat(nodeGenes.get(2).name, is(MAX_NEURONS - 1));
        assertThat(nodeGenes.get(2).type, is(NeuronType.OUTPUT));
        assertThat(nodeGenes.get(2).enabled, is(trueArray));
        assertThat(nodeGenes.get(3).name, is(MAX_NEURONS));
        assertThat(nodeGenes.get(3).type, is(NeuronType.OUTPUT));
        assertThat(nodeGenes.get(3).enabled, is(trueArray));
    }

    @Test
    void initGenePool_initialConnectionConfigurationIsCorrect() {
        // PREPARE
        List<ConnectionGene> connectionGenes = genePool.connectionGenes;

        // VERIFY
        assertThat(connectionGenes.get(0).from.name, is(0));
        assertThat(connectionGenes.get(0).to.name, is(MAX_NEURONS - 1));
        assertThat(connectionGenes.get(0).enabled[0], is(true));
        assertThat(connectionGenes.get(1).from.name, is(0));
        assertThat(connectionGenes.get(1).to.name, is(MAX_NEURONS));
        assertThat(connectionGenes.get(1).enabled[1], is(true));
        assertThat(connectionGenes.get(2).from.name, is(1));
        assertThat(connectionGenes.get(2).to.name, is(MAX_NEURONS - 1));
        assertThat(connectionGenes.get(2).enabled[2], is(true));
        assertThat(connectionGenes.get(3).from.name, is(1));
        assertThat(connectionGenes.get(3).to.name, is(MAX_NEURONS));
        assertThat(connectionGenes.get(3).enabled[3], is(true));
    }

    @Test
    void createPhenotypes_firstPhenotypeFirsNeuronIsCorrect() {
        // PREPARE
        NEATNeuron neuron1 = genePool.phenotypes.get(0).inputNeurons.get(0);
        NEATNeuron neuron2 = genePool.phenotypes.get(0).inputNeurons.get(1);
        NEATNeuron neuron3 = genePool.phenotypes.get(0).outputNeurons.get(0);
        NEATNeuron neuron4 = genePool.phenotypes.get(0).outputNeurons.get(1);


        // VERIFY
        assertThat(neuron1.name, is(0));
        assertThat(neuron1.bias, is(0.0));
        assertThat(neuron1.neuronType, is(NeuronType.INPUT));
        assertThat(neuron1.innerPotential, is(0.0));
        assertThat(neuron2.name, is(1));
        assertThat(neuron2.bias, is(0.0));
        assertThat(neuron2.neuronType, is(NeuronType.INPUT));
        assertThat(neuron2.innerPotential, is(0.0));
        assertThat(neuron3.name, is(999));
        assertThat(neuron3.bias, is(0.0));
        assertThat(neuron3.neuronType, is(NeuronType.OUTPUT));
        assertThat(neuron3.innerPotential, is(0.0));
        assertThat(neuron4.name, is(1000));
        assertThat(neuron4.bias, is(0.0));
        assertThat(neuron4.neuronType, is(NeuronType.OUTPUT));
        assertThat(neuron4.innerPotential, is(0.0));
    }

    @Test
    void createPhenotypes_connectionsAreCorrect() {
        // PREPARE
        List<Connection> connections = genePool.phenotypes.get(0).connections;
        List<NEATNeuron> neurons = genePool.phenotypes.get(0).neurons;

        // VERIFY
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
        // PREPARE
        List<Connection> connections = genePool.phenotypes.get(0).connections;
        Phenotype phenotype = genePool.phenotypes.get(0);
        connections.get(0).weight = 1;
        connections.get(1).weight = 1;
        connections.get(2).weight = 1;
        connections.get(3).weight = 1;

        // VERIFY
        assertThat(phenotype.getOutput(new double[]{1, 1}, Util.activationFunctionIdentity(), Util.activationFunctionIdentity()), is(new double[]{2, 2}));
    }

    @Test
    void evaluate_producesCorrectOutputWhenAllWeightsAreZero() {
        // PREPARE
        List<Connection> connections = genePool.phenotypes.get(0).connections;
        Phenotype phenotype = genePool.phenotypes.get(0);
        connections.get(0).weight = 0;
        connections.get(1).weight = 0;
        connections.get(2).weight = 0;
        connections.get(3).weight = 0;

        // VERIFY
        assertThat(phenotype.getOutput(new double[]{1, 1}, Util.activationFunctionIdentity(), Util.activationFunctionIdentity()), is(new double[]{0, 0}));
    }

    @Test
    void evaluate_producesCorrectOutputWhenOneWeightInEachNeuronIsZero() {
        // PREPARE
        List<Connection> connections = genePool.phenotypes.get(0).connections;
        Phenotype phenotype = genePool.phenotypes.get(0);
        connections.get(0).weight = 1;
        connections.get(1).weight = 0;
        connections.get(2).weight = 1;
        connections.get(3).weight = 0;

        // VERIFY
        assertThat(phenotype.getOutput(new double[]{1, 1}, Util.activationFunctionIdentity(), Util.activationFunctionIdentity()), is(new double[]{2, 0}));
    }

    @Test
    void evaluate_producesCorrectOutputWhenAllWeightsOfFirstNeuronAreZero() {
        // PREPARE
        List<Connection> connections = genePool.phenotypes.get(0).connections;
        Phenotype phenotype = genePool.phenotypes.get(0);
        connections.get(0).weight = 0;
        connections.get(1).weight = 0;
        connections.get(2).weight = 1;
        connections.get(3).weight = 1;

        // VERIFY
        assertThat(phenotype.getOutput(new double[]{1, 1}, Util.activationFunctionIdentity(), Util.activationFunctionIdentity()), is(new double[]{1, 1}));
    }

    @Test
    void splitConnection_splitsGivenConnectionCorrectly() {
        // PREPARE
        List<ConnectionGene> connections = genePool.connectionGenes;
        List<NodeGene> nodeGenes = genePool.nodeGenes;

        // EXECUTE
        genePool.splitConnection(connections.get(0));

        // VERIFY
        assertThat(connections.size(), is(6));
        assertThat(genePool.nodeGenes.size(), is(5));
        assertThat(nodeGenes.get(2).type, is(NeuronType.HIDDEN));
        assertThat(nodeGenes.get(2).name, is(2));
        assertThat(connections.get(0).from, is(nodeGenes.get(0)));
        assertThat(connections.get(0).to, is(nodeGenes.get(2)));
        assertThat(connections.get(5).from, is(nodeGenes.get(2)));
        assertThat(connections.get(5).to, is(nodeGenes.get(3)));
    }

    @Test
    void splitConnection_afterSplitPhenotypeIsCreatedCorrectly() {
        // PREPARE
        List<ConnectionGene> connections = genePool.connectionGenes;
        ConnectionGene connectionGene = connections.get(0);

        // EXECUTE
        genePool.splitConnection(connectionGene);
        genePool.activateSplitConnection(connectionGene, 0);
        genePool.createPhenotypes();
        Phenotype phenotype =  genePool.phenotypes.get(0);

        // VERIFY
        assertThat(phenotype.hiddenNeurons.size(), is(1));
        assertThat(phenotype.inputNeurons.size(), is(2));
        assertThat(phenotype.outputNeurons.size(), is(2));
        assertThat(phenotype.connections.size(), is(5));
        assertThat(phenotype.connections.get(0).from.name, is(0));
        assertThat(phenotype.connections.get(0).to.name, is(2));
        assertThat(phenotype.connections.get(0).from.name, is(0));
        assertThat(phenotype.connections.get(0).to.name, is(2));
    }

    @Test
    void getSplitConnections_returnsSplitConnections() {
        // PREPARE
        assertThat(genePool.getSplitConnections(), is(new ArrayList<>()));
        List<ConnectionGene> connections = genePool.connectionGenes;
        ConnectionGene connectionGene = connections.get(0);

        // EXECUTE
        genePool.splitConnection(connectionGene);

        // VERIFY
        assertThat(genePool.getSplitConnections(), is(List.of(connectionGene)));
    }

    @Test
    void activateSplitConnection_activatesAndInactivatesCorrectConnections() {
        // PREPARE
        List<ConnectionGene> connections = genePool.connectionGenes;
        ConnectionGene connectionGene = connections.get(0);

        // EXECUTE
        genePool.splitConnection(connectionGene);
        genePool.activateSplitConnection(connectionGene, 0);

        // VERIFY
        assertThat(connectionGene.enabled[0], is(false));
        assertThat(connectionGene.firstChild.enabled[0], is(true));
        assertThat(connectionGene.secondChild.enabled[0], is(true));
    }
}
