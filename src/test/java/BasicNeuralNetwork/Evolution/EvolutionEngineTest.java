package BasicNeuralNetwork.Evolution;

import Utils.Util;
import Games.Snake.SnakeGame;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EvolutionEngineTest {

    @Test
    void EvolutionEngine_defaultBuild() {
        EvolutionEngine evolutionEngine = new EvolutionEngine
                .EvolutionEngineBuilder(new int[]{8, 10, 4}, Util.activationFunctionUnitStep(), new SnakeGame(20))
                .build();

        assertThat(evolutionEngine.currentGenerations, is(0));
        assertThat(evolutionEngine.networksGenerated, is(100));
        assertThat(evolutionEngine.maxNumberOfMoves, is(500));
        assertThat(evolutionEngine.totalNumOfNetworks, is(100));
        assertThat(evolutionEngine.neuralNetworks.size(), is(100));
        assertThat(evolutionEngine.networksToKeep, is(40));
        assertThat(evolutionEngine.networksToMutate, is(40));
        assertThat(evolutionEngine.numOfNeuronsToMutate, is(1));
        assertThat(evolutionEngine.numOfMutations, is(1));

        assertThat(evolutionEngine.neuralNetworks.get(0).hiddenLayerActivationFunc, is(Util.activationFunctionUnitStep()));
        assertThat(evolutionEngine.neuralNetworks.get(0).outputLayerActivationFunc, is(Util.activationFunctionUnitStep()));
        assertThat(evolutionEngine.neuralNetworks.get(0).name, is("0"));
        assertThat(evolutionEngine.neuralNetworks.get(1).name, is("1"));
    }
}
