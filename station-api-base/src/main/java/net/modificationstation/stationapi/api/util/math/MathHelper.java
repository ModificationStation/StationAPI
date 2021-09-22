package net.modificationstation.stationapi.api.util.math;

public class MathHelper {

    public static int lerp(double delta, int start, int end) {
        return (int) Math.round(start + (end - start) * delta);
    }
}
