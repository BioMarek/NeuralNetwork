package NEAT;

import Games.Snake.SnakeGame;
import NEAT.Evolution.GenePool;
import NEAT.Evolution.GenePool.GenePoolBuilder;
import NEAT.Evolution.Genotype;
import NEAT.Phenotype.Phenotype;
import Utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GenePoolTest {
    private final static int MAX_NEURONS = 1000;
    private final static double[] INPUT = new double[]{1.0, 1.0};
    private final GenePoolBuilder genePoolBuilder = new GenePoolBuilder(2, 2, Util.activationFunctionIdentity(), new SnakeGame(20));
    protected GenePool genePool;

    @BeforeEach
    void init() {
        genePool = genePoolBuilder.build();
    }

    @Test
    void initGenePool_afterBuildFirstGenotypeIsCorrect() {
        // PREPARE
        var nodeGenes = genePool.genotypes.get(0).nodeGenes;
        var connectionGenes = genePool.genotypes.get(0).connectionGenes;

        // VERIFY
        assertThat(nodeGenes.size(), is(4));
        assertThat(connectionGenes.size(), is(4));

        assertThat(nodeGenes.get(0).name, is(0));
        assertThat(nodeGenes.get(1).name, is(1));
        assertThat(nodeGenes.get(2).name, is(MAX_NEURONS - 1));
        assertThat(nodeGenes.get(3).name, is(MAX_NEURONS));

        assertThat(connectionGenes.get(0).from.name, is(0));
        assertThat(connectionGenes.get(0).to.name, is(MAX_NEURONS - 1));
        assertThat(connectionGenes.get(0).enabled, is(true));
        assertThat(connectionGenes.get(1).from.name, is(0));
        assertThat(connectionGenes.get(1).to.name, is(MAX_NEURONS));
        assertThat(connectionGenes.get(1).enabled, is(true));

        assertThat(connectionGenes.get(2).from.name, is(1));
        assertThat(connectionGenes.get(2).to.name, is(MAX_NEURONS - 1));
        assertThat(connectionGenes.get(2).enabled, is(true));
        assertThat(connectionGenes.get(3).from.name, is(1));
        assertThat(connectionGenes.get(3).to.name, is(MAX_NEURONS));
        assertThat(connectionGenes.get(3).enabled, is(true));
    }

    @Test
    void initGenePool_phenotypeIsCreatedFromGenotypeCorrectly() {
        Genotype genotype = genePool.genotypes.get(0);
        Phenotype phenotype = genotype.createPhenotype();
        var connections = phenotype.connections;

        assertThat(phenotype.inputNeurons.size(), is(2));
        assertThat(phenotype.hiddenNeurons.size(), is(0));
        assertThat(phenotype.outputNeurons.size(), is(2));

        assertThat(connections.get(0).from, is(phenotype.inputNeurons.get(0)));
        assertThat(connections.get(0).to, is(phenotype.outputNeurons.get(0)));
        assertThat(connections.get(1).from, is(phenotype.inputNeurons.get(0)));
        assertThat(connections.get(1).to, is(phenotype.outputNeurons.get(1)));

        assertThat(connections.get(2).from, is(phenotype.inputNeurons.get(1)));
        assertThat(connections.get(2).to, is(phenotype.outputNeurons.get(0)));
        assertThat(connections.get(3).from, is(phenotype.inputNeurons.get(1)));
        assertThat(connections.get(3).to, is(phenotype.outputNeurons.get(1)));
    }

    @Test
    void getOutput_phenotypeReturnsCorrectOutputWhenWeightsToLeftOutputAreOne() {
        Genotype genotype = genePool.genotypes.get(0);
        Phenotype phenotype = genotype.createPhenotype();
        var connections = phenotype.connections;
        connections.get(0).weight = 1;
        connections.get(1).weight = 0;
        connections.get(2).weight = 1;
        connections.get(3).weight = 0;

        assertThat(phenotype.getOutput(INPUT), is(new double[]{2.0, 0.0}));
    }

    @Test
    void getOutput_phenotypeReturnsCorrectOutputWhenAllWeightsAreOne() {
        Genotype genotype = genePool.genotypes.get(0);
        Phenotype phenotype = genotype.createPhenotype();
        var connections = phenotype.connections;
        connections.get(0).weight = 1;
        connections.get(1).weight = 1;
        connections.get(2).weight = 1;
        connections.get(3).weight = 1;

        assertThat(phenotype.getOutput(INPUT), is(new double[]{2.0, 2.0}));
    }

    @Test
    void getOutput_phenotypeReturnsCorrectOutputWhenAllWeightsAreZero() {
        Genotype genotype = genePool.genotypes.get(0);
        Phenotype phenotype = genotype.createPhenotype();
        var connections = phenotype.connections;
        connections.get(0).weight = 0;
        connections.get(1).weight = 0;
        connections.get(2).weight = 0;
        connections.get(3).weight = 0;

        assertThat(phenotype.getOutput(INPUT), is(new double[]{0.0, 0.0}));
    }
}
