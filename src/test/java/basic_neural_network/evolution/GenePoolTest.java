package basic_neural_network.evolution;

import games.snake.SnakeGameMultiplayer;
import neat.evolution.GenePool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Settings.GRID_SIZE;
import static utils.Settings.MAX_NUM_OF_FOOD;
import static utils.Settings.NUM_OF_PLAYERS;
import static utils.Settings.TOTAL_NUM_OF_GENOTYPES;

public class GenePoolTest {

    @BeforeEach
    void setup() {
        MAX_NUM_OF_FOOD = 2;
        GRID_SIZE = 4;
    }

    @Test
    void shuffleGenotypesFromSpecies_works() {
        NUM_OF_PLAYERS = 2;
        TOTAL_NUM_OF_GENOTYPES = 100;
        var genePool = new GenePool(8, 4, Util.activationFunctionIdentity(), new SnakeGameMultiplayer());
        genePool.createSpecies();
        var genotypes = genePool.shuffleGenotypesFromSpecies();

        assertThat(genePool.getSpecies().get(0).getSize(), is(90));
        assertThat(genePool.getSpecies().get(1).getSize(), is(10));
        assertThat(genotypes.size(), is(100));
    }

    @Test
    void divideGenotypes_dividesIntoCorrectNumberOfLists() {
        NUM_OF_PLAYERS = 2;
        TOTAL_NUM_OF_GENOTYPES = 100;
        var genePool = new GenePool(8, 4, Util.activationFunctionIdentity(), new SnakeGameMultiplayer());
        var dividedGenotypes = genePool.divideGenotypes(genePool.shuffleGenotypesFromSpecies());

        assertThat(dividedGenotypes.size(), is(50));
        for (int i = 0; i < 50; i++) {
            assertThat(dividedGenotypes.get(i).size(), is(2));
        }
    }

    @Test
    void divideGenotypes_genotypesAreNotResetOrHardCopied() {
        NUM_OF_PLAYERS = 2;
        TOTAL_NUM_OF_GENOTYPES = 100;
        var genePool = new GenePool(8, 4, Util.activationFunctionIdentity(), new SnakeGameMultiplayer());
        renameAllGenotypes(genePool, "xyz");
        var dividedGenotypes = genePool.divideGenotypes(genePool.shuffleGenotypesFromSpecies());
        renameAllGenotypes(genePool, "123");

        assertThat(dividedGenotypes.get(0).get(0).name, is("123"));
    }


    @AfterEach
    void cleanup() {
        MAX_NUM_OF_FOOD = 2;
        GRID_SIZE = 20;
    }

    private void renameAllGenotypes(GenePool genePool, String name) {
        for (var species : genePool.getSpecies()) {
            for (var genotype : species.genotypes) {
                genotype.name = name;
            }
        }
    }
}
