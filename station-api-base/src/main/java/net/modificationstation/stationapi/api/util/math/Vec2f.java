package net.modificationstation.stationapi.api.util.math;

/**
 * An immutable vector composed of 2 floats.
 */
@SuppressWarnings("ClassCanBeRecord")
public class Vec2f {
    public static final Vec2f ZERO = new Vec2f(0.0f, 0.0f);
    public static final Vec2f SOUTH_EAST_UNIT = new Vec2f(1.0f, 1.0f);
    public static final Vec2f EAST_UNIT = new Vec2f(1.0f, 0.0f);
    public static final Vec2f WEST_UNIT = new Vec2f(-1.0f, 0.0f);
    public static final Vec2f SOUTH_UNIT = new Vec2f(0.0f, 1.0f);
    public static final Vec2f NORTH_UNIT = new Vec2f(0.0f, -1.0f);
    public static final Vec2f MAX_SOUTH_EAST = new Vec2f(Float.MAX_VALUE, Float.MAX_VALUE);
    public static final Vec2f MIN_SOUTH_EAST = new Vec2f(Float.MIN_VALUE, Float.MIN_VALUE);
    public final float x;
    public final float y;

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2f multiply(float value) {
        return new Vec2f(this.x * value, this.y * value);
    }

    public float dot(Vec2f vec) {
        return this.x * vec.x + this.y * vec.y;
    }

    public Vec2f add(Vec2f vec) {
        return new Vec2f(this.x + vec.x, this.y + vec.y);
    }

    public Vec2f add(float value) {
        return new Vec2f(this.x + value, this.y + value);
    }

    public boolean equals(Vec2f other) {
        return this.x == other.x && this.y == other.y;
    }

    public Vec2f normalize() {
        float f = MathHelper.sqrt(this.x * this.x + this.y * this.y);
        return f < 1.0E-4f ? ZERO : new Vec2f(this.x / f, this.y / f);
    }

    public float length() {
        return MathHelper.sqrt(this.x * this.x + this.y * this.y);
    }

    public float lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public float distanceSquared(Vec2f vec) {
        float f = vec.x - this.x;
        float g = vec.y - this.y;
        return f * f + g * g;
    }

    public Vec2f negate() {
        return new Vec2f(-this.x, -this.y);
    }
}

