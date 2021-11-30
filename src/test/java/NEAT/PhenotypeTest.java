package NEAT;

import Games.Snake.SnakeGame;
import NEAT.Evolution.GenePool;
import NEAT.Evolution.Genotype;
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
        genePool = genePoolBuilder.build();
        genotype = genePool.getSpecies().get(0).genotypes.get(0);
        phenotype = genotype.createPhenotype();
    }

    @Test
    void getNetworkOutput_calculatesCorrectlyOutputWhenWeightsAreOne() {
        phenotype.connections.get(0).weight = 1;
        phenotype.connections.get(1).weight = 1;
        phenotype.connections.get(2).weight = 1;
        phenotype.connections.get(3).weight = 1;

        assertThat(phenotype.getNetworkOutput(new double[]{1, 0}), is(new double[]{1.0, 1.0}));
        phenotype.outputNeurons.get(0).innerPotential = 0;
        phenotype.outputNeurons.get(1).innerPotential = 0;
        assertThat(phenotype.getNetworkOutput(new double[]{1, 1}), is(new double[]{2.0, 2.0}));
        phenotype.outputNeurons.get(0).innerPotential = 0;
        phenotype.outputNeurons.get(1).innerPotential = 0;
        assertThat(phenotype.getNetworkOutput(new double[]{0, 0}), is(new double[]{0.0, 0.0}));
    }

    @Test
    void getNetworkOutput_calculatesCorrectlyOutputWhenSomeWeightsAreZero() {
        phenotype.connections.get(0).weight = 1;
        phenotype.connections.get(1).weight = 1;
        phenotype.connections.get(2).weight = 0;
        phenotype.connections.get(3).weight = 0;

        assertThat(phenotype.getNetworkOutput(new double[]{1, 0}), is(new double[]{1.0, 1.0}));
        phenotype.outputNeurons.get(0).innerPotential = 0;
        phenotype.outputNeurons.get(1).innerPotential = 0;
        assertThat(phenotype.getNetworkOutput(new double[]{1, 1}), is(new double[]{1.0, 1.0}));
        phenotype.outputNeurons.get(0).innerPotential = 0;
        phenotype.outputNeurons.get(1).innerPotential = 0;
        assertThat(phenotype.getNetworkOutput(new double[]{0, 0}), is(new double[]{0.0, 0.0}));
    }

    @Test
    void getNetworkOutput_calculatesCorrectlyOutputWhenWeightsAreZero() {
        phenotype.connections.get(0).weight = 0;
        phenotype.connections.get(1).weight = 0;
        phenotype.connections.get(2).weight = 0;
        phenotype.connections.get(3).weight = 0;

        assertThat(phenotype.getNetworkOutput(new double[]{1, 0}), is(new double[]{0.0, 0.0}));
        phenotype.outputNeurons.get(0).innerPotential = 0;
        phenotype.outputNeurons.get(1).innerPotential = 0;
        assertThat(phenotype.getNetworkOutput(new double[]{1, 1}), is(new double[]{0.0, 0.0}));
        phenotype.outputNeurons.get(0).innerPotential = 0;
        phenotype.outputNeurons.get(1).innerPotential = 0;
        assertThat(phenotype.getNetworkOutput(new double[]{0, 0}), is(new double[]{0.0, 0.0}));
    }
}
