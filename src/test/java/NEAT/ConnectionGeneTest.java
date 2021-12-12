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
    protected Genotype genotype;
    private ConnectionGene connectionGene;

    @BeforeEach
    void init() {
        genePool = genePoolBuilder.build();
        genotype = genePool.getSpecies().get(0).genotypes.get(0);
        connectionGene = genotype.connectionGenes.get(0);
    }

    @Test
    void copy_CreatesCorrectCopy() {
        ConnectionGene copy = connectionGene.copy();

        assertThat(copy.equals(connectionGene), is(true));
        assertThat(copy.from, is(connectionGene.from));
        assertThat(copy.to, is(connectionGene.to));
    }
}
