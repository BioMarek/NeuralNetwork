package NEAT;

import Games.Snake.SnakeGame;
import NEAT.Evolution.GenePool;
import NEAT.Evolution.Genotype;
import NEAT.Phenotype.Connection;
import NEAT.Phenotype.Phenotype;
import Utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PhenotypeTest {
    private final GenePool.GenePoolBuilder genePoolBuilder = new GenePool.GenePoolBuilder(2, 2, Util.activationFunctionIdentity(), new SnakeGame(20));
    protected GenePool genePool;
    private Genotype genotype;
    private Phenotype phenotype;

    @BeforeEach
    void init() {
        genePool = genePoolBuilder
                .setOutputLayerActivationFunc(Util.activationFunctionIdentity())
                .build();
        genotype = genePool.getSpecies().get(0).genotypes.get(0);
        phenotype = genotype.createPhenotype();
    }

    @Test
    void getNetworkOutput_calculatesCorrectlyOutputWhenWeightsAreOne() {
        phenotype.connections.get(0).weight = 1;
        phenotype.connections.get(1).weight = 1;
        phenotype.connections.get(2).weight = 1;
        phenotype.connections.get(3).weight = 1;

        phenotype.resetPhenotype();
        assertThat(phenotype.getNetworkOutput(new double[]{1.0d, 0.0d}), is(new double[]{1.0d, 1.0d}));
        phenotype.resetPhenotype();
        assertThat(phenotype.getNetworkOutput(new double[]{1.0d, 1.0d}), is(new double[]{2.0d, 2.0d}));
        phenotype.resetPhenotype();
        assertThat(phenotype.getNetworkOutput(new double[]{0.0d, 0.0d}), is(new double[]{0.0d, 0.0d}));
    }

    @Test
    void getNetworkOutput_calculatesCorrectlyOutputWhenSomeWeightsAreZero() {
        phenotype.connections.get(0).weight = 1;
        phenotype.connections.get(1).weight = 1;
        phenotype.connections.get(2).weight = 0;
        phenotype.connections.get(3).weight = 0;

        assertThat(phenotype.getNetworkOutput(new double[]{1.0d, 0.0d}), is(new double[]{1.0d, 1.0d}));
        phenotype.resetPhenotype();
        assertThat(phenotype.getNetworkOutput(new double[]{1.0d, 1.0d}), is(new double[]{1.0d, 1.0d}));
        phenotype.resetPhenotype();
        assertThat(phenotype.getNetworkOutput(new double[]{0.0d, 0.0d}), is(new double[]{0.0d, 0.0d}));
    }

    @Test
    void getNetworkOutput_calculatesCorrectlyOutputWhenWeightsAreZero() {
        phenotype.connections.get(0).weight = 0;
        phenotype.connections.get(1).weight = 0;
        phenotype.connections.get(2).weight = 0;
        phenotype.connections.get(3).weight = 0;

        assertThat(phenotype.getNetworkOutput(new double[]{1.0d, 0.0d}), is(new double[]{0.0d, 0.0d}));
        phenotype.resetPhenotype();
        assertThat(phenotype.getNetworkOutput(new double[]{1.0d, 1.0d}), is(new double[]{0.0d, 0.0d}));
        phenotype.resetPhenotype();
        assertThat(phenotype.getNetworkOutput(new double[]{0.0d, 0.0d}), is(new double[]{0.0d, 0.0d}));
    }

    @Test
    void referenceGenotype_createsCorrectPhenotype() {
        Genotype genotype = Genotype.referenceGenotype(genePool);
        Phenotype phenotype = genotype.createPhenotype();
        assertThat(phenotype.connections.size(), is(96));
        assertThat(phenotype.inputNeurons.size(), is(8));
        assertThat(phenotype.hiddenNeurons.size(), is(8));
        assertThat(phenotype.outputNeurons.size(), is(4));

        for (Connection connection : phenotype.connections) {
            connection.weight = 1;
        }

        assertThat(phenotype.getNetworkOutput(new double[]{1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d}), is(new double[]{64.0d, 64.0d, 64.0d, 64.0d}));
    }
}
