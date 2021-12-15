package NEAT;

import Games.Snake.SnakeGame;
import NEAT.Evolution.ConnectionGene;
import NEAT.Evolution.GenePool;
import NEAT.Evolution.Genotype;
import NEAT.Evolution.NodeGene;
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
    void genotype_isInitializedCorrectly() {
        assertThat(genotype.inputNodes.get(0).name, is(0));
        assertThat(genotype.inputNodes.get(0).layer, is(0));
        assertThat(genotype.inputNodes.get(1).name, is(1));
        assertThat(genotype.inputNodes.get(1).layer, is(0));
        genotype.printConnections();

        assertThat(genotype.inputNodes.get(0).connectionGenes.size(), is(2));
        assertThat(genotype.inputNodes.get(0).connectionGenes.get(0).to.name, is(999));
        assertThat(genotype.inputNodes.get(0).connectionGenes.get(1).to.name, is(1000));

        assertThat(genotype.inputNodes.get(1).connectionGenes.size(), is(2));
        assertThat(genotype.inputNodes.get(1).connectionGenes.get(0).to.name, is(999));
        assertThat(genotype.inputNodes.get(1).connectionGenes.get(1).to.name, is(1000));

        assertThat(genotype.nodeGenes.get(2).name, is(999));
        assertThat(genotype.nodeGenes.get(2).layer, is(1000));
        assertThat(genotype.nodeGenes.get(3).name, is(1000));
        assertThat(genotype.nodeGenes.get(3).layer, is(1000));

        assertThat(genotype.nodeGenes.get(2).connectionGenes.size(), is(0));
        assertThat(genotype.nodeGenes.get(3).connectionGenes.size(), is(0));

        assertThat(genotype.score, is(0));
        assertThat(genotype.age, is(0));
        assertThat(genotype.name, is("0"));
    }

//    @Test
//    void getOutput_phenotypeReturnsCorrectOutputWhenWeightsToLeftOutputAreOne() {
//        Phenotype phenotype = genotype.createPhenotype();
//        var connections = phenotype.connections;
//        connections.get(0).weight = 1;
//        connections.get(1).weight = 0;
//        connections.get(2).weight = 1;
//        connections.get(3).weight = 0;
//
//        assertThat(phenotype.getNetworkOutput(INPUT), is(new double[]{2.0d, 0.0d}));
//    }
//
//    @Test
//    void getOutput_phenotypeReturnsCorrectOutputWhenAllWeightsAreOne() {
//        Phenotype phenotype = genotype.createPhenotype();
//        var connections = phenotype.connections;
//        connections.get(0).weight = 1;
//        connections.get(1).weight = 1;
//        connections.get(2).weight = 1;
//        connections.get(3).weight = 1;
//
//        assertThat(phenotype.getNetworkOutput(INPUT), is(new double[]{2.0d, 2.0d}));
//    }
//
//    @Test
//    void getOutput_phenotypeReturnsCorrectOutputWhenAllWeightsAreZero() {
//        Phenotype phenotype = genotype.createPhenotype();
//        var connections = phenotype.connections;
//        connections.get(0).weight = 0;
//        connections.get(1).weight = 0;
//        connections.get(2).weight = 0;
//        connections.get(3).weight = 0;
//
//        assertThat(phenotype.getNetworkOutput(INPUT), is(new double[]{0.0d, 0.0d}));
//    }

    @Test
    void addNode_addingNodeWorksCorrectly(){
        genotype.addNode(genotype.connectionGenes.get(0));

        assertThat(genotype.nodeGenes.size(), is(5));
        assertThat(genotype.connectionGenes.size(), is(5));

        assertThat(genotype.nodeGenes.get(0).connectionGenes.size(), is(2));
        assertThat(genotype.nodeGenes.get(0).connectionGenes.get(0).to, is(genotype.nodeGenes.get(4)));
        assertThat(genotype.nodeGenes.get(0).connectionGenes.get(1).to, is(genotype.nodeGenes.get(2)));

        assertThat(genotype.nodeGenes.get(2).connectionGenes.size(), is(1));
        assertThat(genotype.nodeGenes.get(2).connectionGenes.get(0).to, is(genotype.nodeGenes.get(3)));
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
        assertThat(allPossibleConnections.get(2).getSecond().name, is(3));
        assertThat(allPossibleConnections.get(4).getFirst().name, is(3));
        assertThat(allPossibleConnections.get(4).getSecond().name, is(999));
        assertThat(allPossibleConnections.get(6).getFirst().name, is(2));
        assertThat(allPossibleConnections.get(6).getSecond().name, is(1000));
    }

    @Test
    void addConnection_worksCorrectly() {
        genotype.addNode(genotype.connectionGenes.get(0));
        assertThat(genotype.connectionGenes.size(), is(5));

        genotype.addConnection();
        assertThat(genotype.getPossibleConnections().size(), is(2));
        assertThat(genotype.connectionGenes.size(), is(6));

        genotype.addConnection();
        assertThat(genotype.getPossibleConnections().size(), is(1));
        assertThat(genotype.connectionGenes.size(), is(7));

        genotype.addConnection();
        assertThat(genotype.getPossibleConnections().size(), is(0));
        assertThat(genotype.connectionGenes.size(), is(8));

        genotype.addConnection();
        assertThat(genotype.getPossibleConnections().size(), is(0));
        assertThat(genotype.connectionGenes.size(), is(8));
    }

    @Test
    void resetScore_worksCorrectly() {
        genotype.score = 10;
        assertThat(genotype.score, is(10));
        genotype.resetScore();
        assertThat(genotype.score, is(0));
    }

    @Test
    void compare_worksCorrectly() {
        Genotype secondGenotype = genePool.getSpecies().get(0).genotypes.get(1);
        assertThat(genotype.compareTo(secondGenotype), is(0));

        secondGenotype.score = 1;
        assertThat(genotype.compareTo(secondGenotype), is(-1));

        secondGenotype.score = 1;
        genotype.score = 2;
        assertThat(genotype.compareTo(secondGenotype), is(1));
    }
}
