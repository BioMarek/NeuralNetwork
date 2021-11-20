package NEAT;

import Games.Snake.SnakeGame;
import NEAT.Evolution.ConnectionGene;
import NEAT.Evolution.GenePool;
import NEAT.Evolution.Genotype;
import Utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ConnectionGeneTest {
    private final GenePool.GenePoolBuilder genePoolBuilder = new GenePool.GenePoolBuilder(2, 2, Util.activationFunctionIdentity(), new SnakeGame(20));
    protected GenePool genePool;
    private ConnectionGene connectionGene;

    @BeforeEach
    void init() {
        genePool = genePoolBuilder.build();
        connectionGene = genePool.genotypes.get(0).connectionGenes.get(0);
    }

    @Test
    void copy_CreatesCorrectCopy(){
        ConnectionGene copy = connectionGene.copy();

        assertThat(copy.equals(connectionGene), is(true));

        copy.weight = -1;
        assertThat(copy.equals(connectionGene), is(false));
        assertThat(connectionGene.weight, is(not(-1)));
        copy.enabled = false;
        assertThat(connectionGene.enabled, is(not(false)));

    }
}
