package NEAT;

import Games.Snake.SnakeGame;
import NEAT.Evolution.ConnectionGene;
import NEAT.Evolution.GenePool;
import NEAT.Evolution.GenePool.GenePoolBuilder;
import NEAT.Evolution.Genotype;
import NEAT.Phenotype.Phenotype;
import Utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class GenePoolTest {
    private final static int MAX_NEURONS = 1000;

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
        // PREPARE
        Genotype genotype = genePool.genotypes.get(0);
        Phenotype phenotype = genotype.createPhenotype();
        var connections = phenotype.connections;

        // VERIFY
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
    void splitConnection_correctlyCreatesNewConnections() {
        // PREPARE
        Genotype genotype = genePool.genotypes.get(0);
        genotype.splitConnection(genotype.connectionGenes.get(0));

        // VERIFY
        genotype.printConnections();
        assertThat(genotype.connectionGenes.size(), is(5));

        assertThat(genotype.connectionGenes.get(0).from.name, is(0));
        assertThat(genotype.connectionGenes.get(0).to.name, is(2));
        assertThat(genotype.connectionGenes.get(0).weight, is(1.0));
        assertThat(genotype.connectionGenes.get(0).enabled, is(true));

        assertThat(genotype.connectionGenes.get(4).from.name, is(2));
        assertThat(genotype.connectionGenes.get(4).to.name, is(999));
        assertThat(genotype.connectionGenes.get(4).enabled, is(true));
    }

    @Test
    void mutateWeight_changesWeight(){
        // PREPARE
        Genotype genotype = genePool.genotypes.get(0);
        ConnectionGene connectionGene = genotype.connectionGenes.get(0);
        double oldWeight = connectionGene.weight;
        genotype.mutateWeight(connectionGene);

        // VERIFY
        assertThat(connectionGene.weight, is(not(oldWeight)));
    }
}
