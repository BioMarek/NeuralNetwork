package NeuralNetwork;

import java.util.Random;

public class Util {
    static private final Random random = new Random();

    public static double randomDouble(int from, int to){
        return (to - from) * random.nextDouble() + from;
    }

    public static double randomDouble(){
        return random.nextDouble() * 2.0D - 1.0D;
    }

    public static double[] randomDoubleArray(int size) {
        return random.doubles(size, -1.0D, 1.0D).toArray();
    }

    public static double activationFunctionUnitStep(double innerPotential) {
        return innerPotential >= 0.0D ? 1.0D : 0.0D;
    }

    public static double activationFunctionSigmoid(double innerPotential, double steepness) {
        return 1.0D / (1.0D + Math.exp(-steepness * innerPotential));
    }

    public static double activationFunctionSigmoidPrime(double innerPotential) {
        double sigmoid = activationFunctionSigmoid(innerPotential, 1.0D);
        return sigmoid * (1.0D - sigmoid);
    }

    public static double activationFunctionHyperbolicTangent(double innerPotential) {
        double eToNegativeIP = Math.exp(-innerPotential);
        return (1.0D - eToNegativeIP) / (1.0D + eToNegativeIP);
    }
}
