package net.modificationstation.stationapi.api.util.math;

public class Vector2f {
    public static final Vector2f ZERO = new Vector2f(0.0F, 0.0F);
    public static final Vector2f SOUTH_EAST_UNIT = new Vector2f(1.0F, 1.0F);
    public static final Vector2f EAST_UNIT = new Vector2f(1.0F, 0.0F);
    public static final Vector2f WEST_UNIT = new Vector2f(-1.0F, 0.0F);
    public static final Vector2f SOUTH_UNIT = new Vector2f(0.0F, 1.0F);
    public static final Vector2f NORTH_UNIT = new Vector2f(0.0F, -1.0F);
    public static final Vector2f MAX_SOUTH_EAST = new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
    public static final Vector2f MIN_SOUTH_EAST = new Vector2f(Float.MIN_VALUE, Float.MIN_VALUE);
    public final float x;
    public final float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Vector2f other) {
        return this.x == other.x && this.y == other.y;
    }
}
