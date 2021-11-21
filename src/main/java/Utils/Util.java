package Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
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

    public static double activationFunctionSigmoidPrime(double innerPotential) {
        double sigmoid = activationFunctionSigmoid(innerPotential, 1.0D);
        return sigmoid * (1.0D - sigmoid);
    }

    public static Function<Double, Double> activationFunctionHyperbolicTangent() {
        return (Double innerPotential) -> {
            double eToNegativeIP = Math.exp(-innerPotential);
            return (1.0D - eToNegativeIP) / (1.0D + eToNegativeIP);
        };
    }

    public static double[] primitiveDoubleArrayFromList(List<Double> list){
        int length = list.size();
        double[] result = new double[length];
        for (int i = 0; i < length; i++){
            result[i] = list.get(i);
        }
        return result;
    }
}
