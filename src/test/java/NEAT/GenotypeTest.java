package NEAT;

import Games.Snake.SnakeGame;
import NEAT.Evolution.ConnectionGene;
import NEAT.Evolution.GenePool;
import NEAT.Evolution.Genotype;
import NEAT.Evolution.NodeGene;
import NEAT.Phenotype.Phenotype;
import Utils.Pair;
import Utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GenotypeTest {
    private final static double[] INPUT = new double[]{1.0d, 1.0d};

    private final GenePool.GenePoolBuilder genePoolBuilder = new GenePool.GenePoolBuilder(2, 2, Util.activationFunctionIdentity(), new SnakeGame(20));
    protected GenePool genePool;
    private Genotype genotype;

    @BeforeEach
    void init() {
        genePool = genePoolBuilder.build();
        genotype = genePool.getSpecies().get(0).genotypes.get(0);
    }

    @Test
    void getOutput_phenotypeReturnsCorrectOutputWhenWeightsToLeftOutputAreOne() {
        Phenotype phenotype = genotype.createPhenotype();
        var connections = phenotype.connections;
        connections.get(0).weight = 1;
        connections.get(1).weight = 0;
        connections.get(2).weight = 1;
        connections.get(3).weight = 0;

        assertThat(phenotype.getNetworkOutput(INPUT), is(new double[]{2.0d, 0.0d}));
    }

    @Test
    void getOutput_phenotypeReturnsCorrectOutputWhenAllWeightsAreOne() {
        Phenotype phenotype = genotype.createPhenotype();
        var connections = phenotype.connections;
        connections.get(0).weight = 1;
        connections.get(1).weight = 1;
        connections.get(2).weight = 1;
        connections.get(3).weight = 1;

        assertThat(phenotype.getNetworkOutput(INPUT), is(new double[]{2.0d, 2.0d}));
    }

    @Test
    void getOutput_phenotypeReturnsCorrectOutputWhenAllWeightsAreZero() {
        Phenotype phenotype = genotype.createPhenotype();
        var connections = phenotype.connections;
        connections.get(0).weight = 0;
        connections.get(1).weight = 0;
        connections.get(2).weight = 0;
        connections.get(3).weight = 0;

        assertThat(phenotype.getNetworkOutput(INPUT), is(new double[]{0.0d, 0.0d}));
    }

    @Test
    void copy_createdCopyIsCorrect() {
        Genotype copy = genotype.copy();
        List<ConnectionGene> copyConnectionGenes = copy.connectionGenes;
        List<ConnectionGene> genotypeConnectionGenes = genotype.connectionGenes;
        for (int i = 0; i < genotypeConnectionGenes.size(); i++) {
            assertThat(copyConnectionGenes.get(i), is(genotypeConnectionGenes.get(i)));
        }
        assertThat(copy.nodeGenes, is(genotype.nodeGenes));
        assertThat(copy.score, is(genotype.score));
    }

    @Test
    void referenceGenotype_providesCorrectGenotype() {
        Genotype genotype = Genotype.referenceGenotype(genePool);
        assertThat(genotype.connectionGenes.size(), is(96));
        assertThat((int) genotype.nodeGenes.stream().filter(nodeGene -> nodeGene.type == NeuronType.INPUT).count(), is(8));
        assertThat((int) genotype.nodeGenes.stream().filter(nodeGene -> nodeGene.type == NeuronType.HIDDEN).count(), is(8));
        assertThat((int) genotype.nodeGenes.stream().filter(nodeGene -> nodeGene.type == NeuronType.OUTPUT).count(), is(4));
    }

    @Test
    void getPossibleConnections_afterOneConnectionIsAdded() {
        genotype.addNode(genotype.connectionGenes.get(0));
        List<Pair<NodeGene>> allPossibleConnections = genotype.getPossibleConnections();

        assertThat(allPossibleConnections.size(), is(3));
        assertThat(allPossibleConnections.get(0).getFirst().name, is(0));
        assertThat(allPossibleConnections.get(0).getSecond().name, is(999));
        assertThat(allPossibleConnections.get(1).getFirst().name, is(1));
        assertThat(allPossibleConnections.get(1).getSecond().name, is(2));
        assertThat(allPossibleConnections.get(2).getFirst().name, is(2));
        assertThat(allPossibleConnections.get(2).getSecond().name, is(1000));
    }

    @Test
    void getPossibleConnections_afterTwoConnectionsAreAdded() {
        genotype.addNode(genotype.connectionGenes.get(0));
        genotype.addNode(genotype.connectionGenes.get(0));
        List<Pair<NodeGene>> allPossibleConnections = genotype.getPossibleConnections();

        assertThat(allPossibleConnections.size(), is(7));
        assertThat(allPossibleConnections.get(0).getFirst().name, is(0));
        assertThat(allPossibleConnections.get(0).getSecond().name, is(2));
        assertThat(allPossibleConnections.get(2).getFirst().name, is(1));
        assertThat(allPossibleConnections.get(2).getSecond().name, is(2));
        assertThat(allPossibleConnections.get(4).getFirst().name, is(2));
        assertThat(allPossibleConnections.get(4).getSecond().name, is(1000));
        assertThat(allPossibleConnections.get(5).getFirst().name, is(3));
        assertThat(allPossibleConnections.get(5).getSecond().name, is(999));
    }
}
