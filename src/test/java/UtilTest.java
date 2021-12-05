import Utils.Util;
import org.junit.jupiter.api.Test;

import java.util.stream.DoubleStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilTest {
    private static final int TEST_REPEATS = 100;

    @Test
    void randomInt_returnsValuesInCorrectBounds() {
        for (int i = 0; i < TEST_REPEATS; i++) {
            int result = Util.randomInt(2, 5);
            assertThat(result >= 2 && result < 5, is(true));
        }
    }

    @Test
    void randomInt_returnsValuesInCorrectNegativeBounds() {
        for (int i = 0; i < TEST_REPEATS; i++) {
            int result = Util.randomInt(-5, 1);
            assertThat(result >= -5 && result < 1, is(true));
            result = Util.randomInt(-5, -1);
            assertThat(result >= -5 && result < -1, is(true));
        }
    }

    @Test
    void randomInt_throwsExceptionWhenUpperBoundIsSmallerThanLowerBound() {
        assertThrows(IllegalArgumentException.class, () -> Util.randomInt(5, 2));
    }

    @Test
    void randomDouble_returnsCorrectValues() {
        for (int i = 0; i < TEST_REPEATS; i++) {
            double result = Util.randomDouble();
            assertThat(result >= -1.0D && result < 1.0D, is(true));
        }
    }

    @Test
    void randomDouble_returnsValuesInCorrectBounds() {
        for (int i = 0; i < TEST_REPEATS; i++) {
            double result = Util.randomDouble(2, 5);
            assertThat(result >= 2.0D && result < 5.0D, is(true));
        }
    }

    @Test
    void randomDouble_returnsValuesInCorrectNegativeBounds() {
        for (int i = 0; i < TEST_REPEATS; i++) {
            double result = Util.randomDouble(-5, 1);
            assertThat(result >= -5.0D && result < 1.0D, is(true));
            result = Util.randomDouble(-5, -1);
            assertThat(result >= -5.0D && result < -1.0D, is(true));
        }
    }

    @Test
    void randomDouble_throwsExceptionWhenUpperBoundIsSmallerThanLowerBound() {
        assertThrows(IllegalArgumentException.class, () -> Util.randomDouble(5, 2));
    }

    @Test
    void randomDouble_randomDoubleArrayReturnsArrayWithCorrectNumbers() {
        for (int i = 0; i < TEST_REPEATS; i++) {
            double[] array = Util.randomDoubleArray(10);

            assertThat(DoubleStream.of(array).allMatch(j -> j >= -1.0D), is(true));
            assertThat(DoubleStream.of(array).allMatch(j -> j < 1.0D), is(true));
        }
    }

}
