package NEAT;

import Games.Snake.SnakeGame;
import NEAT.Evolution.GenePool;
import NEAT.Evolution.Species;
import Utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SpeciesTest {
    private final GenePool.GenePoolBuilder genePoolBuilder = new GenePool.GenePoolBuilder(2, 2, Util.activationFunctionIdentity(), new SnakeGame(20));
    protected GenePool genePool;
    private Species species;

    @BeforeEach
    void init() {
        genePool = genePoolBuilder.build();
        species = genePool.getSpecies().get(0);
    }

    @Test
    void getAverage_returnsCorrectValues() {
        species.size = 2;
        species.genotypes.get(0).score = 10;
        species.calculateAverage();
        assertThat(species.average, is(5.0));
    }

    @Test
    void reduceSizeBy_returnsCorrectValues() {
        genePool.speciesReduction = 0.3;
        int reduction = species.reduceSize();
        assertThat(species.size, is(70));
        assertThat(reduction, is(30));

        genePool.speciesReduction = 0.9;
        species.size = 90;
        reduction = species.reduceSize();
        assertThat(species.size, is(9));
        assertThat(reduction, is(81));

        genePool.speciesReduction = 0.1;
        species.size = 15;
        reduction = species.reduceSize();
        assertThat(species.size, is(14));
        assertThat(reduction, is(1));

        genePool.speciesReduction = 0.1;
        species.size = 29;
        reduction = species.reduceSize();
        assertThat(species.size, is(27));
        assertThat(reduction, is(2));

        genePool.speciesReduction = 0.1;
        species.size = 71;
        reduction = species.reduceSize();
        assertThat(species.size, is(64));
        assertThat(reduction, is(7));
    }
}
