package net.modificationstation.stationapi.api.util.math;

import java.util.EnumSet;

import static net.minecraft.util.maths.MathHelper.*;

/**
 * An immutable vector composed of 3 doubles.
 * 
 * <p>This vector class is used for representing position, velocity,
 * rotation, color, etc.
 * 
 * <p>This vector has proper {@link #hashCode()} and {@link #equals(Object)}
 * implementations and can be used as a map key.
 *
 * @see Vec3i
 * @see Vec3f
 */
public class Vec3d implements Position {
    /**
     * The zero vector (0, 0, 0).
     */
    public static final Vec3d ZERO = new Vec3d(0.0, 0.0, 0.0);
    /**
     * The X coordinate of this vector.
     */
    public final double x;
    /**
     * The Y coordinate of this vector.
     */
    public final double y;
    /**
     * The Z coordinate of this vector.
     */
    public final double z;

    /**
     * Converts a packed RGB color into a vector of (red, green, blue).
     * 
     * @return the vector representing the given color; each coordinate has
     * value between 0 and 1
     * 
     * @param rgb the color in the 0xRRGGBB format
     */
    public static Vec3d unpackRgb(int rgb) {
        double d = (double)(rgb >> 16 & 0xFF) / 255.0;
        double e = (double)(rgb >> 8 & 0xFF) / 255.0;
        double f = (double)(rgb & 0xFF) / 255.0;
        return new Vec3d(d, e, f);
    }

    /**
     * Creates a vector representing the center of the given block position.
     */
    public static Vec3d ofCenter(Vec3i vec) {
        return new Vec3d((double)vec.getX() + 0.5, (double)vec.getY() + 0.5, (double)vec.getZ() + 0.5);
    }

    /**
     * Copies the given vector.
     */
    public static Vec3d of(Vec3i vec) {
        return new Vec3d(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * Creates a vector representing the bottom center of the given block
     * position.
     * 
     * <p>The bottom center of a block position {@code pos} is
     * {@code (pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5)}.
     * 
     * @see #ofCenter(Vec3i)
     */
    public static Vec3d ofBottomCenter(Vec3i vec) {
        return new Vec3d((double)vec.getX() + 0.5, vec.getY(), (double)vec.getZ() + 0.5);
    }

    /**
     * Creates a vector representing the center of the given block position but
     * with the given offset for the Y coordinate.
     * 
     * @return a vector of {@code (vec.getX() + 0.5, vec.getY() + deltaY,
     * vec.getZ() + 0.5)}
     */
    public static Vec3d ofCenter(Vec3i vec, double deltaY) {
        return new Vec3d((double)vec.getX() + 0.5, (double)vec.getY() + deltaY, (double)vec.getZ() + 0.5);
    }

    /**
     * Creates a vector of the given coordinates.
     */
    public Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Copies the given vector.
     */
    public Vec3d(Vec3f vec) {
        this(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * Subtracts this vector from the given vector.
     * 
     * @see #subtract(Vec3d)
     * @return the difference between the given vector and this vector
     */
    public Vec3d relativize(Vec3d vec) {
        return new Vec3d(vec.x - this.x, vec.y - this.y, vec.z - this.z);
    }

    /**
     * Normalizes this vector.
     * 
     * <p>Normalized vector is a vector with the same direction but with
     * length 1. Each coordinate of normalized vector has value between 0
     * and 1.
     * 
     * @return the normalized vector of this vector
     */
    public Vec3d normalize() {
        double d = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (d < 1.0E-4) {
            return ZERO;
        }
        return new Vec3d(this.x / d, this.y / d, this.z / d);
    }

    /**
     * Returns the dot product of this vector and the given vector.
     */
    public double dotProduct(Vec3d vec) {
        return this.x * vec.x + this.y * vec.y + this.z * vec.z;
    }

    /**
     * Returns the cross product of this vector and the given vector.
     */
    public Vec3d crossProduct(Vec3d vec) {
        return new Vec3d(this.y * vec.z - this.z * vec.y, this.z * vec.x - this.x * vec.z, this.x * vec.y - this.y * vec.x);
    }

    /**
     * Subtracts the given vector from this vector.
     * 
     * @see #subtract(double, double, double)
     * @see #relativize(Vec3d)
     * @return the difference between this vector and the given vector
     */
    public Vec3d subtract(Vec3d vec) {
        return this.subtract(vec.x, vec.y, vec.z);
    }

    /**
     * Subtracts the given vector from this vector.
     * 
     * @see #relativize(Vec3d)
     * @return the difference between this vector and the given vector
     */
    public Vec3d subtract(double x, double y, double z) {
        return this.add(-x, -y, -z);
    }

    /**
     * Returns the sum of this vector and the given vector.
     * 
     * @see #add(double, double, double)
     */
    public Vec3d add(Vec3d vec) {
        return this.add(vec.x, vec.y, vec.z);
    }

    /**
     * Returns the sum of this vector and the given vector.
     * 
     * @see #add(Vec3d)
     */
    public Vec3d add(double x, double y, double z) {
        return new Vec3d(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Checks if the distance between this vector and the given position is
     * less than {@code radius}.
     */
    public boolean isInRange(Position pos, double radius) {
        return this.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) < radius * radius;
    }

    /**
     * Returns the distance between this vector and the given vector.
     * 
     * @see #squaredDistanceTo(Vec3d)
     */
    public double distanceTo(Vec3d vec) {
        double d = vec.x - this.x;
        double e = vec.y - this.y;
        double f = vec.z - this.z;
        return Math.sqrt(d * d + e * e + f * f);
    }

    /**
     * Returns the squared distance between this vector and the given vector.
     * 
     * <p>Can be used for fast comparison between distances.
     * 
     * @see #squaredDistanceTo(double, double, double)
     * @see #distanceTo(Vec3d)
     */
    public double squaredDistanceTo(Vec3d vec) {
        double d = vec.x - this.x;
        double e = vec.y - this.y;
        double f = vec.z - this.z;
        return d * d + e * e + f * f;
    }

    /**
     * Returns the squared distance between this vector and the given vector.
     * 
     * <p>Can be used for fast comparison between distances.
     * 
     * @see #squaredDistanceTo(Vec3d)
     * @see #distanceTo(Vec3d)
     */
    public double squaredDistanceTo(double x, double y, double z) {
        double d = x - this.x;
        double e = y - this.y;
        double f = z - this.z;
        return d * d + e * e + f * f;
    }

    /**
     * Return a vector whose coordinates are the coordinates of this vector
     * each multiplied by the given scalar value.
     * 
     * @see #multiply(Vec3d)
     * @see #multiply(double, double, double)
     */
    public Vec3d multiply(double value) {
        return this.multiply(value, value, value);
    }

    /**
     * Creates a vector with the same length but with the opposite direction.
     */
    public Vec3d negate() {
        return this.multiply(-1.0);
    }

    /**
     * Returns a vector whose coordinates are the product of each pair of
     * coordinates in this vector and the given vector.
     * 
     * @see #multiply(double, double, double)
     * @see #multiply(double)
     */
    public Vec3d multiply(Vec3d vec) {
        return this.multiply(vec.x, vec.y, vec.z);
    }

    /**
     * Returns a vector whose coordinates are the product of each pair of
     * coordinates in this vector and the given vector.
     * 
     * @see #multiply(Vec3d)
     * @see #multiply(double)
     */
    public Vec3d multiply(double x, double y, double z) {
        return new Vec3d(this.x * x, this.y * y, this.z * z);
    }

    /**
     * {@return the length of this vector}
     * 
     * <p>The length of a vector is equivalent to the distance between that
     * vector and the {@linkplain #ZERO} vector.
     * 
     * @see #lengthSquared()
     */
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
     * {@return the squared length of this vector}
     * 
     * <p>Can be used for fast comparison between lengths.
     * 
     * @see #length()
     */
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    /**
     * {@return the horizontal length of this vector}
     * 
     * <p>This length is same as the length of a 2-vector with the {@link #x} and
     * {@link #z} components of this vector, or the euclidean distance between
     * {@code (x, z)} and the origin.
     * 
     * @see #horizontalLengthSquared()
     */
    public double horizontalLength() {
        return Math.sqrt(this.x * this.x + this.z * this.z);
    }

    /**
     * {@return the squared horizontal length of this vector}
     * 
     * <p>Can be used for fast comparison between horizontal lengths.
     * 
     * @see #horizontalLength()
     */
    public double horizontalLengthSquared() {
        return this.x * this.x + this.z * this.z;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vec3d)) {
            return false;
        }
        Vec3d vec3d = (Vec3d)o;
        if (Double.compare(vec3d.x, this.x) != 0) {
            return false;
        }
        if (Double.compare(vec3d.y, this.y) != 0) {
            return false;
        }
        return Double.compare(vec3d.z, this.z) == 0;
    }

    public int hashCode() {
        long l = Double.doubleToLongBits(this.x);
        int i = (int)(l ^ l >>> 32);
        l = Double.doubleToLongBits(this.y);
        i = 31 * i + (int)(l ^ l >>> 32);
        l = Double.doubleToLongBits(this.z);
        i = 31 * i + (int)(l ^ l >>> 32);
        return i;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    /**
     * Performs linear interpolation from this vector to the given vector.
     * 
     * @param delta the interpolation coefficient in the range between 0 and 1
     * @param to the vector to interpolate to
     */
    public Vec3d lerp(Vec3d to, double delta) {
        return new Vec3d(MathHelper.lerp(delta, this.x, to.x), MathHelper.lerp(delta, this.y, to.y), MathHelper.lerp(delta, this.z, to.z));
    }

    /**
     * Rotates this vector by the given angle counterclockwise around the X axis.
     * 
     * @param angle the angle in radians
     */
    public Vec3d rotateX(float angle) {
        float f = cos(angle);
        float g = sin(angle);
        double e = this.y * (double)f + this.z * (double)g;
        double h = this.z * (double)f - this.y * (double)g;
        return new Vec3d(this.x, e, h);
    }

    /**
     * Rotates this vector by the given angle counterclockwise around the Y axis.
     * 
     * @param angle the angle in radians
     */
    public Vec3d rotateY(float angle) {
        float f = cos(angle);
        float g = sin(angle);
        double d = this.x * (double)f + this.z * (double)g;
        double h = this.z * (double)f - this.x * (double)g;
        return new Vec3d(d, this.y, h);
    }

    /**
     * Rotates this vector by the given angle counterclockwise around the Z axis.
     * 
     * @param angle the angle in radians
     */
    public Vec3d rotateZ(float angle) {
        float f = cos(angle);
        float g = sin(angle);
        double d = this.x * (double)f + this.y * (double)g;
        double e = this.y * (double)f - this.x * (double)g;
        return new Vec3d(d, e, this.z);
    }

    /**
     * Converts pitch and yaw into a direction vector.
     * 
     * @see #fromPolar(float, float)
     * 
     * @param polar the vector composed of pitch and yaw
     */
    public static Vec3d fromPolar(Vec2f polar) {
        return Vec3d.fromPolar(polar.x, polar.y);
    }

    /**
     * Converts pitch and yaw into a direction vector.
     * 
     * @see #fromPolar(Vec2f)
     */
    public static Vec3d fromPolar(float pitch, float yaw) {
        float f = cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float g = sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float h = -cos(-pitch * ((float)Math.PI / 180));
        float i = sin(-pitch * ((float)Math.PI / 180));
        return new Vec3d(g * h, i, f * h);
    }

    /**
     * Applies the floor function to the coordinates chosen by the given axes.
     */
    public Vec3d floorAlongAxes(EnumSet<Direction.Axis> axes) {
        double d = axes.contains(Direction.Axis.X) ? (double) floor(this.x) : this.x;
        double e = axes.contains(Direction.Axis.Y) ? (double) floor(this.y) : this.y;
        double f = axes.contains(Direction.Axis.Z) ? (double) floor(this.z) : this.z;
        return new Vec3d(d, e, f);
    }

    /**
     * Returns the coordinate chosen by the given axis.
     */
    public double getComponentAlongAxis(Direction.Axis axis) {
        return axis.choose(this.x, this.y, this.z);
    }

    public Vec3d withAxis(Direction.Axis axis, double value) {
        double d = axis == Direction.Axis.X ? value : this.x;
        double e = axis == Direction.Axis.Y ? value : this.y;
        double f = axis == Direction.Axis.Z ? value : this.z;
        return new Vec3d(d, e, f);
    }

    @Override
    public final double getX() {
        return this.x;
    }

    @Override
    public final double getY() {
        return this.y;
    }

    @Override
    public final double getZ() {
        return this.z;
    }
}

