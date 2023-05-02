package neat;

import games.snake.SnakeGame;
import neat.evolution.GenePool;
import neat.evolution.Genotype;
import neat.evolution.Species;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Settings;
import utils.Util;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class SpeciesTest {
    protected GenePool genePool;
    private Species species;

    @BeforeEach
    void init() {
        Settings.CHANCE_HARD_MUTATE_WEIGHT = 1;
        Settings.CHANCE_ADD_NODE = 0;
        Settings.NETWORKS_TO_KEEP = 0.3;
        genePool = new GenePool(2, 2, Util.activationFunctionIdentity(), new SnakeGame());
        species = genePool.getSpecies().get(0);
    }

    @Test
    void calculateAverage_returnsCorrectValues() {
        species.genotypes.get(0).score = 100;
        species.calculateAverage();
        assertThat(species.average, is(1.0));
    }

    @Test
    void reduceSizeBy_returnsCorrectValues() {
        Settings.SPECIES_REDUCTION = 0.3d;
        int reduction = species.reduceSize();
        assertThat(species.genotypes.size(), is(70));
        assertThat(reduction, is(30));

        Settings.SPECIES_REDUCTION = 0.9d;
        reduction = species.reduceSize();
        assertThat(species.genotypes.size(), is(7));
        assertThat(reduction, is(63));

        Settings.SPECIES_REDUCTION = 0.1d;
        reduction = species.reduceSize();
        assertThat(species.genotypes.size(), is(5));
        assertThat(reduction, is(2));

        Settings.SPECIES_REDUCTION = 0.1d;
        reduction = species.reduceSize();
        assertThat(species.genotypes.size(), is(3));
        assertThat(reduction, is(2));
    }

    @Test
    void increaseSize_sizeIsIncreasedCorrectly() {
        species.increaseSize(20);
        assertThat(species.genotypes.size(), is(120));
    }

    @Test
    void mutateSpecies_mutatesSpeciesCorrectly() {
        List<Genotype> oldGenotypes = new ArrayList<>();
        for (Genotype genotype : species.genotypes)
            oldGenotypes.add(genotype.copy());
        species.mutateSpecies();

        assertThat(species.genotypes.get(0).connectionGenes, is(oldGenotypes.get(0).connectionGenes));
        assertThat(species.genotypes.get(0).nodeGenes, is(oldGenotypes.get(0).nodeGenes));
        assertThat(species.genotypes.get(29).connectionGenes, is(oldGenotypes.get(29).connectionGenes));
        assertThat(species.genotypes.get(29).nodeGenes, is(oldGenotypes.get(29).nodeGenes));
        assertThat(species.genotypes.get(30).connectionGenes, is(not(oldGenotypes.get(30).connectionGenes)));
        assertThat(species.genotypes.get(30).nodeGenes, is(not(oldGenotypes.get(30).nodeGenes)));
    }

    @Test
    void increaseAge_addsOne() {
        species.increaseAge();
        assertThat(species.age, is(1));
    }

    @Test
    void isExtinct_worksCorrectly() {
        assertThat(species.isExtinct(), is(false));
        species.genotypes = new ArrayList<>();
        assertThat(species.isExtinct(), is(true));
    }

    @Test
    void compare_worksCorrectly() {
        Species speciesCopy = new Species(genePool, species.genotypes, 1);
        assertThat(species.compareTo(speciesCopy), is(0));

        speciesCopy.average = 1.0d;
        assertThat(species.compareTo(speciesCopy), is(-1));

        speciesCopy.average = -1.0d;
        assertThat(species.compareTo(speciesCopy), is(1));
    }
}
