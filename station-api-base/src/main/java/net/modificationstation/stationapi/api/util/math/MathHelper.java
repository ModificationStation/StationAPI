package net.modificationstation.stationapi.api.util.math;

public class MathHelper {

    public static int lerp(double delta, int start, int end) {
        return (int) Math.round(start + (end - start) * delta);
    }

    public static float lerp(double delta, float start, float end) {
        return (float) (start + (end - start) * delta);
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
}
