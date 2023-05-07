package neat;

import games.snake.SnakeGameMultiplayer;
import neat.evolution.GenePool;
import neat.evolution.Genotype;
import neat.phenotype.Phenotype;
import utils.Settings;
import utils.Util;
import visualizations.dtos.VisualizationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PhenotypeTest {
    protected GenePool genePool;
    private Genotype genotype;
    private Phenotype phenotype;

    @BeforeEach
    void init() {
        Settings.MAX_NUM_OF_FOOD = 2;
        Settings.GRID_ROWS = 4;
        Settings.GRID_COLUMNS = 4;
        Settings.PIXELS_PER_SQUARE = 1;
        Settings.LEAVE_CORPSE = false;
        genePool = new GenePool(2, 2, Util.activationFunctionIdentity(), new SnakeGameMultiplayer());
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
    void getVisualizationDTO_phenotypeWithoutAddedConnections() {
        // TODO parametrized tests
        VisualizationDTO visualizationDTO = phenotype.getVisualizationDTO();
        assertThat(visualizationDTO.layers.size(), is(2));
        assertThat(visualizationDTO.layers.get(0).neurons.get(0).name, is(0));
        assertThat(visualizationDTO.layers.get(0).neurons.get(1).name, is(1));
        assertThat(visualizationDTO.layers.get(1).neurons.get(0).name, is(999));
        assertThat(visualizationDTO.layers.get(1).neurons.get(1).name, is(1000));

        assertThat(visualizationDTO.layers.get(0).neurons.get(0).layer, is(0));
        assertThat(visualizationDTO.layers.get(0).neurons.get(1).layer, is(0));
        assertThat(visualizationDTO.layers.get(1).neurons.get(0).layer, is(1));
        assertThat(visualizationDTO.layers.get(1).neurons.get(1).layer, is(1));

        assertThat(visualizationDTO.layers.get(0).neurons.get(0).position, is(0));
        assertThat(visualizationDTO.layers.get(0).neurons.get(1).position, is(1));
        assertThat(visualizationDTO.layers.get(1).neurons.get(0).position, is(0));
        assertThat(visualizationDTO.layers.get(1).neurons.get(1).position, is(1));
    }

    @Test
    void getVisualizationDTO_phenotypeWithOneAddedConnection() {
        genotype.addNode(genotype.connectionGenes.get(0));
        phenotype = genotype.createPhenotype();
        VisualizationDTO visualizationDTO = phenotype.getVisualizationDTO();
        assertThat(visualizationDTO.layers.size(), is(3));
        assertThat(visualizationDTO.layers.get(0).neurons.get(0).name, is(0));
        assertThat(visualizationDTO.layers.get(0).neurons.get(1).name, is(1));
        assertThat(visualizationDTO.layers.get(1).neurons.get(0).name, is(2));
        assertThat(visualizationDTO.layers.get(2).neurons.get(0).name, is(999));
        assertThat(visualizationDTO.layers.get(2).neurons.get(1).name, is(1000));

        assertThat(visualizationDTO.layers.get(0).neurons.get(0).layer, is(0));
        assertThat(visualizationDTO.layers.get(0).neurons.get(1).layer, is(0));
        assertThat(visualizationDTO.layers.get(1).neurons.get(0).layer, is(1));
        assertThat(visualizationDTO.layers.get(2).neurons.get(0).layer, is(2));
        assertThat(visualizationDTO.layers.get(2).neurons.get(1).layer, is(2));

        assertThat(visualizationDTO.layers.get(0).neurons.get(0).position, is(0));
        assertThat(visualizationDTO.layers.get(0).neurons.get(1).position, is(1));
        assertThat(visualizationDTO.layers.get(1).neurons.get(0).position, is(0));
        assertThat(visualizationDTO.layers.get(2).neurons.get(0).position, is(0));
        assertThat(visualizationDTO.layers.get(2).neurons.get(1).position, is(1));
    }

    @Test
    void getVisualizationDTO_phenotypeWithTwoAddedConnection() {
        genotype.addNode(genotype.connectionGenes.get(0));
        genotype.addNode(genotype.connectionGenes.get(0));

        phenotype = genotype.createPhenotype();
        VisualizationDTO visualizationDTO = phenotype.getVisualizationDTO();
        assertThat(visualizationDTO.layers.size(), is(4));
        assertThat(visualizationDTO.layers.get(0).neurons.get(0).name, is(0));
        assertThat(visualizationDTO.layers.get(0).neurons.get(1).name, is(1));
        assertThat(visualizationDTO.layers.get(1).neurons.get(0).name, is(3));
        assertThat(visualizationDTO.layers.get(2).neurons.get(0).name, is(2));
        assertThat(visualizationDTO.layers.get(3).neurons.get(0).name, is(999));
        assertThat(visualizationDTO.layers.get(3).neurons.get(1).name, is(1000));

        assertThat(visualizationDTO.layers.get(0).neurons.get(0).layer, is(0));
        assertThat(visualizationDTO.layers.get(0).neurons.get(1).layer, is(0));
        assertThat(visualizationDTO.layers.get(1).neurons.get(0).layer, is(1));
        assertThat(visualizationDTO.layers.get(2).neurons.get(0).layer, is(2));
        assertThat(visualizationDTO.layers.get(3).neurons.get(0).layer, is(3));
        assertThat(visualizationDTO.layers.get(3).neurons.get(1).layer, is(3));

        assertThat(visualizationDTO.layers.get(0).neurons.get(0).position, is(0));
        assertThat(visualizationDTO.layers.get(0).neurons.get(1).position, is(1));
        assertThat(visualizationDTO.layers.get(1).neurons.get(0).position, is(0));
        assertThat(visualizationDTO.layers.get(2).neurons.get(0).position, is(0));
        assertThat(visualizationDTO.layers.get(3).neurons.get(0).position, is(0));
        assertThat(visualizationDTO.layers.get(3).neurons.get(1).position, is(1));
    }
}
