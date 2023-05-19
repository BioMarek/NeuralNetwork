package neural_network.evolution;

import games.snake.SnakeGameMultiplayer;
import neat.evolution.GenePool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Settings;
import utils.Util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class GenePoolTest {

    @BeforeEach
    void setup() {
        Settings.MAX_NUM_OF_FOOD = 2;
        Settings.GRID_ROWS = 4;
        Settings.GRID_COLUMNS = 4;
        Settings.PIXELS_PER_SQUARE = 1;
    }

    @Test
    void shuffleGenotypesFromSpecies_works() {
        Settings.NUM_OF_PLAYERS = 2;
        Settings.TOTAL_NUM_OF_GENOTYPES = 100;
        Settings.HIDDEN_LAYER_ACTIVATION_FUNC = Util.activationFunctionIdentity();
        Settings.OUTPUT_LAYER_ACTIVATION_FUNC = Util.activationFunctionIdentity();
        var genePool = new GenePool(8, 4, new SnakeGameMultiplayer());
        genePool.createSpecies();
        var genotypes = genePool.shuffleGenotypesFromSpecies();

        assertThat(genePool.getSpecies().get(0).getSize(), is(90));
        assertThat(genePool.getSpecies().get(1).getSize(), is(10));
        assertThat(genotypes.size(), is(100));
    }

    @Test
    void divideGenotypes_dividesIntoCorrectNumberOfLists() {
        Settings.NUM_OF_PLAYERS = 2;
        Settings.TOTAL_NUM_OF_GENOTYPES = 100;
        Settings.HIDDEN_LAYER_ACTIVATION_FUNC = Util.activationFunctionIdentity();
        Settings.OUTPUT_LAYER_ACTIVATION_FUNC = Util.activationFunctionIdentity();
        var genePool = new GenePool(8, 4, new SnakeGameMultiplayer());
        var dividedGenotypes = genePool.divideGenotypes(genePool.shuffleGenotypesFromSpecies());

        assertThat(dividedGenotypes.size(), is(50));
        for (int i = 0; i < 50; i++) {
            assertThat(dividedGenotypes.get(i).size(), is(2));
        }
    }

    @Test
    void divideGenotypes_genotypesAreNotResetOrHardCopied() {
        Settings.NUM_OF_PLAYERS = 2;
        Settings.TOTAL_NUM_OF_GENOTYPES = 100;
        Settings.HIDDEN_LAYER_ACTIVATION_FUNC = Util.activationFunctionIdentity();
        Settings.OUTPUT_LAYER_ACTIVATION_FUNC = Util.activationFunctionIdentity();
        var genePool = new GenePool(8, 4, new SnakeGameMultiplayer());
        renameAllGenotypes(genePool, "xyz");
        var dividedGenotypes = genePool.divideGenotypes(genePool.shuffleGenotypesFromSpecies());
        renameAllGenotypes(genePool, "123");

        assertThat(dividedGenotypes.get(0).get(0).name, is("123"));
    }


    @AfterEach
    void cleanup() {
        Settings.MAX_NUM_OF_FOOD = 2;
        Settings.GRID_ROWS = 20;
        Settings.GRID_COLUMNS = 20;
        Settings.PIXELS_PER_SQUARE = 20;
    }

    private void renameAllGenotypes(GenePool genePool, String name) {
        for (var species : genePool.getSpecies()) {
            for (var genotype : species.genotypes) {
                genotype.name = name;
            }
        }
    }
}
