package net.modificationstation.stationapi.api.util.math;

import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;

public class MathHelper {

    public static final float SQUARE_ROOT_OF_TWO = sqrt(2.0F);
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};

    public static float sqrt(float value) {
        return (float)Math.sqrt(value);
    }

    public static double square(double n) {
        return n * n;
    }

    public static int lerp(double delta, int start, int end) {
        return (int) Math.round(start + (end - start) * delta);
    }

    public static float lerp(double delta, float start, float end) {
        return (float) (start + (end - start) * delta);
    }

    public static double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }

    public static float interpolate2D(
            double x, double y,
            float v1, float v2, float v3, float v4
    ) {
        return lerp(y, lerp(x, v1, v2), lerp(x, v3, v4));
    }

    public static float interpolate3D(
            double x, double y, double z,
            float v1, float v2, float v3, float v4, float v5, float v6, float v7, float v8
    ) {
        return lerp(z, interpolate2D(x, y, v1, v2, v3, v4), interpolate2D(x, y, v5, v6, v7, v8));
    }

    public static long hashCode(int x, int y, int z) {
        long l = (x * 3129871L) ^ (long)z * 116129781L ^ (long)y;
        l = l * l * 42317861L + l * 11L;
        return l >> 16;
    }

    public static int smallestEncompassingPowerOfTwo(int value) {
        int i = value - 1;
        i |= i >> 1;
        i |= i >> 2;
        i |= i >> 4;
        i |= i >> 8;
        i |= i >> 16;
        return i + 1;
    }

    public static float clamp(float value, float min, float max) {
        return Floats.constrainToRange(value, min, max);
    }

    public static int clamp(int value, int min, int max) {
        return Ints.constrainToRange(value, min, max);
    }

    public static float fastInverseSqrt(float x) {
        float f = 0.5F * x;
        int i = Float.floatToIntBits(x);
        i = 1597463007 - (i >> 1);
        x = Float.intBitsToFloat(i);
        x *= 1.5F - f * x * x;
        return x;
    }

    public static float fastInverseCbrt(float x) {
        int i = Float.floatToIntBits(x);
        i = 1419967116 - i / 3;
        float f = Float.intBitsToFloat(i);
        f = 0.6666667F * f + 1.0F / (3.0F * f * f * x);
        f = 0.6666667F * f + 1.0F / (3.0F * f * f * x);
        return f;
    }

    public static boolean approximatelyEquals(float a, float b) {
        return Math.abs(b - a) < 1.0E-5F;
    }

    public static boolean isPowerOfTwo(int i) {
        return i != 0 && (i & i - 1) == 0;
    }

    /**
     * {@return ceil(log<sub>2</sub>({@code value}))}
     *
     * <p>The vanilla implementation uses the de Bruijn sequence.
     *
     * @see Integer#numberOfLeadingZeros(int)
     *
     * @param value the input value
     */
    public static int ceilLog2(int value) {
        value = MathHelper.isPowerOfTwo(value) ? value : MathHelper.smallestEncompassingPowerOfTwo(value);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)((long)value * 125613361L >> 27) & 0x1F];
    }

    /**
     * {@return floor(log<sub>2</sub>({@code value}))}
     *
     * <p>The vanilla implementation uses the de Bruijn sequence.
     *
     * @see Integer#numberOfLeadingZeros(int)
     *
     * @param value the input value
     */
    public static int floorLog2(int value) {
        return ceilLog2(value) - (isPowerOfTwo(value) ? 0 : 1);
    }

    public static int idealHash(int i) {
        i ^= i >>> 16;
        i *= -2048144789;
        i ^= i >>> 13;
        i *= -1028477387;
        i ^= i >>> 16;
        return i;
    }

    /**
     * Returns a value farther than or as far as {@code value} from zero that
     * is a multiple of {@code divisor}.
     */
    public static int roundUpToMultiple(int value, int divisor) {
        return MathHelper.ceilDiv(value, divisor) * divisor;
    }

    public static int ceilDiv(int a, int b) {
        return -Math.floorDiv(-a, b);
    }
}
