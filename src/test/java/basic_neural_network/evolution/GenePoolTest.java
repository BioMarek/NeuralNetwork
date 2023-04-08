package basic_neural_network.evolution;

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
        Settings.numOfApples = 2;
        Settings.gridSize = 4;
    }

    @Test
    void shuffleGenotypesFromSpecies_works() {
        Settings.numOfPlayers = 2;
        Settings.totalNumOfGenotypes = 100;
        var genePool = new GenePool(8, 4, Util.activationFunctionIdentity(), new SnakeGameMultiplayer());
        genePool.createSpecies();
        var genotypes = genePool.shuffleGenotypesFromSpecies();

        assertThat(genePool.getSpecies().get(0).getSize(), is(90));
        assertThat(genePool.getSpecies().get(1).getSize(), is(10));
        assertThat(genotypes.size(), is(100));
    }

    @Test
    void divideGenotypes_dividesIntoCorrectNumberOfLists() {
        Settings.numOfPlayers = 2;
        Settings.totalNumOfGenotypes = 100;
        var genePool = new GenePool(8, 4, Util.activationFunctionIdentity(), new SnakeGameMultiplayer());
        var dividedGenotypes = genePool.divideGenotypes(genePool.shuffleGenotypesFromSpecies());

        assertThat(dividedGenotypes.size(), is(50));
        for (int i = 0; i < 50; i++) {
            assertThat(dividedGenotypes.get(i).size(), is(2));
        }
    }

    @Test
    void divideGenotypes_genotypesAreNotResetOrHardCopied() {
        Settings.numOfPlayers = 2;
        Settings.totalNumOfGenotypes = 100;
        var genePool = new GenePool(8, 4, Util.activationFunctionIdentity(), new SnakeGameMultiplayer());
        renameAllGenotypes(genePool, "xyz");
        var dividedGenotypes = genePool.divideGenotypes(genePool.shuffleGenotypesFromSpecies());
        renameAllGenotypes(genePool, "123");

        assertThat(dividedGenotypes.get(0).get(0).name, is("123"));
    }


    @AfterEach
    void cleanup() {
        Settings.numOfApples = 2;
        Settings.gridSize = 20;
    }

    private void renameAllGenotypes(GenePool genePool, String name) {
        for (var species : genePool.getSpecies()) {
            for (var genotype : species.genotypes) {
                genotype.name = name;
            }
        }
    }
}
