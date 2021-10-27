package Snake;

/**
 * DTO used to transfer state of snake to {@link NeuralNetwork}. DistanceToFood variables represent number of moves in
 * particular direction to food. Moving to direction that increases distance from food is designed as negative board
 * size irrespective of actual distance. SafeVariables say whether move to that direction results in death (-1) or is
 * safe (1)
 */
public class SnakeDTO {
    int leftDistanceToFood;
    int rightDistanceToFood;
    int upDistanceToFood;
    int downDistanceToFood;

    int leftSafe;
    int rightSafe;
    int upSafe;
    int downSafe;

    public double[] getInput() {
        double[] result = new double[8];
        result[0] = leftDistanceToFood;
        result[1] = rightDistanceToFood;
        result[2] = upDistanceToFood;
        result[3] = downDistanceToFood;

        result[4] = leftSafe;
        result[5] = rightSafe;
        result[6] = upSafe;
        result[7] = downSafe;

        return result;
    }
}
