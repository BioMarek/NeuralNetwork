package NEAT;

import Games.Snake.SnakeGame;
import NEAT.Evolution.ConnectionGene;
import NEAT.Evolution.GenePool;
import NEAT.Evolution.Genotype;
import NEAT.Phenotype.Phenotype;
import Utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GenotypeTest {
    private final static double[] INPUT = new double[]{1.0, 1.0};

    private final GenePool.GenePoolBuilder genePoolBuilder = new GenePool.GenePoolBuilder(2, 2, Util.activationFunctionIdentity(), new SnakeGame(20));
    protected GenePool genePool;
    private Genotype genotype;

    @BeforeEach
    void init() {
        genePool = genePoolBuilder.build();
        genotype = genePool.getGenotypes().get(0);
    }

    @Test
    void getOutput_phenotypeReturnsCorrectOutputWhenWeightsToLeftOutputAreOne() {
        Phenotype phenotype = genotype.createPhenotype();
        var connections = phenotype.connections;
        connections.get(0).weight = 1;
        connections.get(1).weight = 0;
        connections.get(2).weight = 1;
        connections.get(3).weight = 0;

        assertThat(phenotype.getNetworkOutput(INPUT), is(new double[]{2.0, 0.0}));
    }

    @Test
    void getOutput_phenotypeReturnsCorrectOutputWhenAllWeightsAreOne() {
        Phenotype phenotype = genotype.createPhenotype();
        var connections = phenotype.connections;
        connections.get(0).weight = 1;
        connections.get(1).weight = 1;
        connections.get(2).weight = 1;
        connections.get(3).weight = 1;

        assertThat(phenotype.getNetworkOutput(INPUT), is(new double[]{2.0, 2.0}));
    }

    @Test
    void getOutput_phenotypeReturnsCorrectOutputWhenAllWeightsAreZero() {
        Phenotype phenotype = genotype.createPhenotype();
        var connections = phenotype.connections;
        connections.get(0).weight = 0;
        connections.get(1).weight = 0;
        connections.get(2).weight = 0;
        connections.get(3).weight = 0;

        assertThat(phenotype.getNetworkOutput(INPUT), is(new double[]{0.0, 0.0}));
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
        assertThat(copy.hiddenLayerActivationFunc, is(genotype.hiddenLayerActivationFunc));
        assertThat(copy.outputLayerActivationFunc, is(genotype.outputLayerActivationFunc));
        assertThat(copy.score, is(genotype.score));
    }
}
