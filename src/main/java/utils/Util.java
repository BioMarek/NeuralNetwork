package utils;

import games.snake.SnakeMap;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Util {
    static private final Random random = new Random();

    /**
     * The function returns double integer in [from, to) interval.
     *
     * @param from lower bound, inclusive
     * @param to   upper bound, exclusive
     * @return random double in [from, to) interval
     */
    public static double randomDouble(int from, int to) {
        if (to < from)
            throw new IllegalArgumentException(String.format("Upper bound '%d' cannot be smaller than lower bound '%d'.", to, from));

        return (to - from) * random.nextDouble() + from;
    }

    public static double randomDouble(double from, double to) {
        if (to < from)
            throw new IllegalArgumentException(String.format("Upper bound '%f' cannot be smaller than lower bound '%f'.", to, from));

        return (to - from) * random.nextDouble() + from;
    }

    /**
     * The function returns random integer in [from, to) interval.
     *
     * @param from lower bound, inclusive
     * @param to   upper bound, exclusive
     * @return random integer in [from, to) interval
     */
    public static int randomInt(int from, int to) {
        if (to < from)
            throw new IllegalArgumentException(String.format("Upper bound '%d' cannot be smaller than lower bound '%d'.", to, from));

        return from + random.nextInt(to - from);
    }

    /**
     * @return random double from [-1, 1] interval.
     */
    public static double randomDouble() {
        return random.nextDouble() * 2.0D - 1.0D;
    }

    /**
     * The function returns whether trial with supplied chance was successful.
     *
     * @param chance that true will be returned
     * @return true if trial was success, false otherwise
     */
    public static boolean isRandomChanceTrue(double chance) {
        return Util.random.nextDouble() < chance;
    }

    public static double[] randomDoubleArray(int size) {
        return random.doubles(size, -1.0D, 1.0D).toArray();
    }

    public static double[] doubleArrayOfOnes(int size) {
        double[] array = new double[size];
        Arrays.fill(array, 1);
        return array;
    }

    public static boolean[] randomBooleanArray(int size) {
        boolean[] boolArray = new boolean[size];
        for (int i = 0; i < size; i++) {
            boolArray[i] = random.nextBoolean();
        }
        return boolArray;
    }

    public static boolean[] reverseBooleanArray(boolean[] array) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = !array[i];
        }
        return result;
    }

    public static boolean[] booleanArray(int size, boolean type) {
        boolean[] boolArray = new boolean[size];
        if (type)
            Arrays.fill(boolArray, Boolean.TRUE);
        else
            Arrays.fill(boolArray, Boolean.FALSE);
        return boolArray;
    }

    public static Function<Double, Double> activationFunctionUnitStep() {
        return (Double innerPotential) -> innerPotential >= 0.0D ? 1.0D : 0.0D;
    }

    public static Function<Double, Double> activationFunctionIdentity() {
        return (Double innerPotential) -> innerPotential;
    }

    public static double activationFunctionSigmoid(double innerPotential, double steepness) {
        return 1.0D / (1.0D + Math.exp(-steepness * innerPotential));
    }

    public static Function<Double, Double> activationFunctionSigmoidPrime() {
        return (Double innerPotential) -> {
            var sigmoid = activationFunctionSigmoid(innerPotential, 1.0D);
            return sigmoid * (1.0D - sigmoid);
        };
    }

    public static Function<Double, Double> activationFunctionHyperbolicTangent() {
        return (Double innerPotential) -> {
            var eToNegativeIP = Math.exp(-innerPotential);
            return (1.0D - eToNegativeIP) / (1.0D + eToNegativeIP);
        };
    }

    public static double[] primitiveDoubleArrayFromList(List<Double> list) {
        var length = list.size();
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public static int[][] arrayCopy(int[][] source) {
        int[][] result = new int[source.length][source[0].length];
        for (int row = 0; row < source.length; row++) {
            System.arraycopy(source[row], 0, result[row], 0, source[0].length);
        }
        return result;
    }

    /**
     * Repeats given function n times.
     */
    public static BiConsumer<Integer, Runnable> repeat = (n, function) -> {
        for (int i = 1; i <= n; i++)
            function.run();
    };

    /**
     * Finds random row and column of grid that is ot occupied e.i. doesn't have snake or food.
     *
     * @param grid on which to find coordinates
     * @return pair with unoccupied row and column
     */
    public static Pair<Integer> randomFreeCoordinate(int[][] grid) {
        int row, column;
        do {
            row = Util.randomInt(1, grid.length - 1);
            column = Util.randomInt(1, grid[0].length - 1);
        } while (grid[row][column] != SnakeMap.EMPTY.value);
        return new Pair<>(row, column);
    }

    public static Pair<Integer> randomCoordinate(int[][] grid) {
        return new Pair<>(Util.randomInt(1, grid.length - 1), Util.randomInt(1, grid[0].length - 1));
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateRandomString(int length) {
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }
}
