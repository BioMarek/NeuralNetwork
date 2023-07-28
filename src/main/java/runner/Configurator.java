package runner;

@FunctionalInterface
public interface Configurator {
    /**
     * Configures the game by setting the values in settings.
     *
     * @return true if this configuration is for {@link games.snake.freeEvolution.FEGame}
     */
    boolean configure();
}
