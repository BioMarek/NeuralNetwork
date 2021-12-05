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

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class GenePoolTest {
    private final static int MAX_NEURONS = 1000;

    private final GenePoolBuilder genePoolBuilder = new GenePoolBuilder(2, 2, Util.activationFunctionIdentity(), new SnakeGame(20));
    protected GenePool genePool;

    @BeforeEach
    void init() {
        genePool = genePoolBuilder
                .setSpeciesMinimalReduction(2)
                .build();
    }

    @Test
    void initGenePool_afterBuildFirstGenotypeIsCorrect() {
        // PREPARE
        var nodeGenes = genePool.getSpecies().get(0).genotypes.get(0).nodeGenes;
        var connectionGenes = genePool.getSpecies().get(0).genotypes.get(0).connectionGenes;

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
    void createSpecies_correctlyCreatesSpecies() {
        genePool.createSpecies();
        assertThat(genePool.getSpecies().size(), is(2));
        assertThat(genePool.getSpecies().get(0).getSize(), is(90));
        assertThat(genePool.getSpecies().get(1).getSize(), is(10));

        genePool.createSpecies();
        assertThat(genePool.getSpecies().size(), is(3));
        assertThat(genePool.getSpecies().get(0).getSize(), is(81));
        assertThat(genePool.getSpecies().get(1).getSize(), is(8));
        assertThat(genePool.getSpecies().get(2).getSize(), is(11));
    }

    @Test
    void reduceSpeciesSizesUniformly_reducesSizesCorrectly() {
        genePool.createSpecies();
        genePool.createSpecies();
        int reduction = genePool.reduceSpeciesSizesUniformly();
        assertThat(reduction, is(12));
        assertThat(genePool.getSpecies().get(0).getSize(), is(73));
        assertThat(genePool.getSpecies().get(1).getSize(), is(6));
        assertThat(genePool.getSpecies().get(2).getSize(), is(9));
    }

    @Test
    void removeDeadSpecies_removesSpeciesWithSizeZero() {
        genePool.createSpecies();
        genePool.getSpecies().get(1).genotypes = new ArrayList<>();
        genePool.removeDeadSpecies();
        assertThat(genePool.getSpecies().size(), is(1));
    }

    @Test
    void resetScores_resetsAllScores() {
        genePool.createSpecies();
        genePool.getSpecies().get(0).average = 10.0d;
        genePool.getSpecies().get(1).average = 5.0d;
        genePool.getSpecies().get(0).genotypes.get(0).score = 10;
        genePool.getSpecies().get(0).genotypes.get(50).score = 10;
        genePool.getSpecies().get(1).genotypes.get(1).score = 10;
        genePool.resetScores();

        assertThat(genePool.getSpecies().get(0).average, is(0.0d));
        assertThat(genePool.getSpecies().get(1).average, is(0.0d));
        assertThat(genePool.getSpecies().get(0).genotypes.get(0).score, is(0));
        assertThat(genePool.getSpecies().get(0).genotypes.get(50).score, is(0));
        assertThat(genePool.getSpecies().get(1).genotypes.get(1).score, is(0));
    }

    @Test
    void initGenePool_phenotypeIsCreatedFromGenotypeCorrectly() {
        // PREPARE
        Genotype genotype = genePool.getSpecies().get(0).genotypes.get(0);
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
    void addNode_correctlyCreatesNewConnections() {
        // PREPARE
        Genotype genotype = genePool.getSpecies().get(0).genotypes.get(0);
        genotype.addNode(genotype.connectionGenes.get(0));

        // VERIFY
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
    void mutateWeight_changesWeight() {
        // PREPARE
        Genotype genotype = genePool.getSpecies().get(0).genotypes.get(0);
        ConnectionGene connectionGene = genotype.connectionGenes.get(0);
        double oldWeight = connectionGene.weight;
        genotype.mutateWeight(connectionGene);

        // VERIFY
        assertThat(connectionGene.weight, is(not(oldWeight)));
    }

    @Test
    void nodeNameOfSplitConnection_worksCorrectly() {
        // PREPARE
        Genotype genotype = genePool.getSpecies().get(0).genotypes.get(0);
        ConnectionGene connectionGene = genotype.connectionGenes.get(0);
        genotype.addNode(connectionGene);

        // VERIFY
        assertThat(genePool.nodeNameOfSplitConnection(connectionGene), is(2));
    }
}
