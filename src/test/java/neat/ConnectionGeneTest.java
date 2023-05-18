package neat;

import games.snake.SnakeGameMultiplayer;
import neat.evolution.ConnectionGene;
import neat.evolution.GenePool;
import neat.evolution.Genotype;
import neat.evolution.NodeGene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Settings;
import utils.Util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ConnectionGeneTest {
    protected GenePool genePool;
    protected Genotype genotype;
    private ConnectionGene connectionGene;

    @BeforeEach
    void init() {
        Settings.MAX_NUM_OF_FOOD = 2;
        Settings.GRID_ROWS = 4;
        Settings.GRID_COLUMNS = 4;
        Settings.PIXELS_PER_SQUARE = 1;
        Settings.LEAVE_CORPSE = false;
        Settings.hiddenLayerActivationFunc = Util.activationFunctionIdentity();
        Settings.outputLayerActivationFunc = Util.activationFunctionIdentity();
        genePool = new GenePool(2, 2, new SnakeGameMultiplayer());
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

    @Test
    void compareTo_worksCorrectly() {
        NodeGene inputNode = new NodeGene(NeuronType.INPUT, 0, 0);
        NodeGene hiddenLayer1First = new NodeGene(NeuronType.HIDDEN, 1, 1);
        NodeGene hiddenLayer1Second = new NodeGene(NeuronType.HIDDEN, 3, 1);
        NodeGene hiddenLayer2First = new NodeGene(NeuronType.HIDDEN, 2, 2);
        NodeGene hiddenLayer2Second = new NodeGene(NeuronType.HIDDEN, 4, 2);
        NodeGene hiddenLayer1Third = new NodeGene(NeuronType.HIDDEN, 3, 2);
        NodeGene hiddenLayer2Copy = new NodeGene(NeuronType.HIDDEN, 4, 2);

        assertThat(inputNode.compareTo(hiddenLayer1First), is(-1));
        assertThat(hiddenLayer1First.compareTo(hiddenLayer1Second), is(-1));
        assertThat(hiddenLayer1Second.compareTo(hiddenLayer1First), is(1));
        assertThat(hiddenLayer1Second.compareTo(hiddenLayer2First), is(-1));
        assertThat(hiddenLayer1Second.compareTo(hiddenLayer1Third), is(-1));
        assertThat(hiddenLayer2Second.compareTo(inputNode), is(1));
        assertThat(hiddenLayer2Second.compareTo(hiddenLayer2Copy), is(0));
    }
}
