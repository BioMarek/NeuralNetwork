package runner;

@FunctionalInterface
public interface Configurator {
    /**
     * Configures the game by setting the values in settings.
     */
    void configure();
}
