package net.modificationstation.stationapi.api.util.math;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.Unmodifiable;

import java.util.function.Function;
import java.util.stream.IntStream;

import static net.minecraft.util.maths.MathHelper.floor;
import static net.modificationstation.stationapi.api.util.math.MathHelper.square;

/**
 * A vector composed of 3 integers.
 * 
 * <p>This class is very often used to hold the coordinates. To hold
 * positions for entities and other non-voxels, consider using {@link Vec3f} (that holds
 * values using {@code float}) or {@link Vec3d} (that holds values using {@code double})
 * instead.
 * 
 * @see Vec3f
 * @see Vec3d
 */
@Unmodifiable
public class Vec3i
implements Comparable<Vec3i> {
    public static final Codec<Vec3i> CODEC = Codec.INT_STREAM.comapFlatMap(intStream -> Util.toArray(intStream, 3).map(is -> new Vec3i(is[0], is[1], is[2])), vec3i -> IntStream.of(vec3i.getX(), vec3i.getY(), vec3i.getZ()));
    public static final Vec3i ZERO = new Vec3i(0, 0, 0);
    private int x;
    private int y;
    private int z;

    private static Function<Vec3i, DataResult<Vec3i>> createRangeValidator(int maxAbsValue) {
        return vec -> {
            if (Math.abs(vec.getX()) < maxAbsValue && Math.abs(vec.getY()) < maxAbsValue && Math.abs(vec.getZ()) < maxAbsValue) {
                return DataResult.success(vec);
            }
            return DataResult.error("Position out of range, expected at most " + maxAbsValue + ": " + vec);
        };
    }

    public static Codec<Vec3i> createOffsetCodec(int maxAbsValue) {
        return CODEC.flatXmap(Vec3i.createRangeValidator(maxAbsValue), Vec3i.createRangeValidator(maxAbsValue));
    }

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i(double x, double y, double z) {
        this(floor(x), floor(y), floor(z));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vec3i vec3i)) {
            return false;
        }
        if (this.getX() != vec3i.getX()) {
            return false;
        }
        if (this.getY() != vec3i.getY()) {
            return false;
        }
        return this.getZ() == vec3i.getZ();
    }

    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    @Override
    public int compareTo(Vec3i vec3i) {
        if (this.getY() == vec3i.getY()) {
            if (this.getZ() == vec3i.getZ()) {
                return this.getX() - vec3i.getX();
            }
            return this.getZ() - vec3i.getZ();
        }
        return this.getY() - vec3i.getY();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    protected Vec3i setX(int x) {
        this.x = x;
        return this;
    }

    protected Vec3i setY(int y) {
        this.y = y;
        return this;
    }

    protected Vec3i setZ(int z) {
        this.z = z;
        return this;
    }

    /**
     * {@return another Vec3i whose coordinates have the parameter x, y, and z
     * added to the coordinates of this vector}
     * 
     * <p>This method always returns an immutable object.
     */
    public Vec3i add(double x, double y, double z) {
        if (x == 0.0 && y == 0.0 && z == 0.0) {
            return this;
        }
        return new Vec3i((double)this.getX() + x, (double)this.getY() + y, (double)this.getZ() + z);
    }

    /**
     * {@return another Vec3i whose coordinates have the parameter x, y, and z
     * added to the coordinates of this vector}
     * 
     * <p>This method always returns an immutable object.
     */
    public Vec3i add(int x, int y, int z) {
        if (x == 0 && y == 0 && z == 0) {
            return this;
        }
        return new Vec3i(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    /**
     * {@return another Vec3i whose coordinates have the coordinates of {@code vec}
     * added to the coordinates of this vector}
     * 
     * <p>This method always returns an immutable object.
     */
    public Vec3i add(Vec3i vec) {
        return this.add(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * {@return another Vec3i whose coordinates have the coordinates of {@code vec}
     * subtracted from the coordinates of this vector}
     * 
     * <p>This method always returns an immutable object.
     */
    public Vec3i subtract(Vec3i vec) {
        return this.add(-vec.getX(), -vec.getY(), -vec.getZ());
    }

    /**
     * {@return a vector with all components multiplied by {@code scale}}
     * 
     * @implNote This can return the same vector if {@code scale} equals {@code 1}.
     */
    public Vec3i multiply(int scale) {
        if (scale == 1) {
            return this;
        }
        if (scale == 0) {
            return ZERO;
        }
        return new Vec3i(this.getX() * scale, this.getY() * scale, this.getZ() * scale);
    }

    /**
     * {@return a vector which is offset by {@code 1} in the upward direction}
     */
    public Vec3i up() {
        return this.up(1);
    }

    /**
     * {@return a vector which is offset by {@code distance} in the upward direction}
     * 
     * @implNote This can return the same vector if {@code distance} equals {@code 0}.
     */
    public Vec3i up(int distance) {
        return this.offset(Direction.UP, distance);
    }

    /**
     * {@return a vector which is offset by {@code 1} in the downward direction}
     */
    public Vec3i down() {
        return this.down(1);
    }

    /**
     * {@return a vector which is offset by {@code distance} in the downward direction}
     * 
     * @implNote This can return the same vector if {@code distance} equals {@code 0}.
     */
    public Vec3i down(int distance) {
        return this.offset(Direction.DOWN, distance);
    }

    /**
     * {@return a vector which is offset by {@code 1} in the northward direction}
     */
    public Vec3i north() {
        return this.north(1);
    }

    /**
     * {@return a vector which is offset by {@code distance} in the northward direction}
     * 
     * @implNote This can return the same vector if {@code distance} equals {@code 0}.
     */
    public Vec3i north(int distance) {
        return this.offset(Direction.NORTH, distance);
    }

    /**
     * {@return a vector which is offset by {@code 1} in the southward direction}
     */
    public Vec3i south() {
        return this.south(1);
    }

    /**
     * {@return a vector which is offset by {@code distance} in the southward direction}
     * 
     * @implNote This can return the same vector if {@code distance} equals {@code 0}.
     */
    public Vec3i south(int distance) {
        return this.offset(Direction.SOUTH, distance);
    }

    /**
     * {@return a vector which is offset by {@code 1} in the westward direction}
     */
    public Vec3i west() {
        return this.west(1);
    }

    /**
     * {@return a vector which is offset by {@code distance} in the westward direction}
     * 
     * @implNote This can return the same vector if {@code distance} equals {@code 0}.
     */
    public Vec3i west(int distance) {
        return this.offset(Direction.WEST, distance);
    }

    /**
     * {@return a vector which is offset by {@code 1} in the eastward direction}
     */
    public Vec3i east() {
        return this.east(1);
    }

    /**
     * {@return a vector which is offset by {@code distance} in the eastward direction}
     * 
     * @implNote This can return the same vector if {@code distance} equals {@code 0}.
     */
    public Vec3i east(int distance) {
        return this.offset(Direction.EAST, distance);
    }

    /**
     * {@return a vector which is offset by {@code 1} in {@code direction} direction}
     */
    public Vec3i offset(Direction direction) {
        return this.offset(direction, 1);
    }

    /**
     * {@return a vector which is offset by {@code distance} in {@code direction} direction}
     * 
     * @implNote This can return the same vector if {@code distance} equals {@code 0}.
     */
    public Vec3i offset(Direction direction, int distance) {
        if (distance == 0) {
            return this;
        }
        return new Vec3i(this.getX() + direction.getOffsetX() * distance, this.getY() + direction.getOffsetY() * distance, this.getZ() + direction.getOffsetZ() * distance);
    }

    /**
     * {@return a vector which is offset by {@code distance} on {@code axis} axis}
     * 
     * @implNote This can return the same vector if {@code distance} equals {@code 0}.
     */
    public Vec3i offset(Direction.Axis axis, int distance) {
        if (distance == 0) {
            return this;
        }
        int i = axis == Direction.Axis.X ? distance : 0;
        int j = axis == Direction.Axis.Y ? distance : 0;
        int k = axis == Direction.Axis.Z ? distance : 0;
        return new Vec3i(this.getX() + i, this.getY() + j, this.getZ() + k);
    }

    public Vec3i crossProduct(Vec3i vec) {
        return new Vec3i(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }

    /**
     * {@return whether the distance between here and {@code vec} is less than {@code distance}}
     */
    public boolean isWithinDistance(Vec3i vec, double distance) {
        return this.getSquaredDistance(vec) < square(distance);
    }

    /**
     * {@return whether the distance between here and {@code pos} is less than {@code distance}}
     */
    public boolean isWithinDistance(Position pos, double distance) {
        return this.getSquaredDistance(pos) < square(distance);
    }

    /**
     * {@return the squared distance between here (center) and {@code vec}}
     */
    public double getSquaredDistance(Vec3i vec) {
        return this.getSquaredDistance(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * {@return the squared distance between here and {@code pos}}
     */
    public double getSquaredDistance(Position pos) {
        return this.getSquaredDistanceFromCenter(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * {@return the squared distance between the center of this voxel and {@code (x, y, z)}}
     * This is equivalent to {@link Vec3d#ofCenter(Vec3i)
     * Vec3d.ofCenter(this).squaredDistanceTo(x, y, z)}.
     */
    public double getSquaredDistanceFromCenter(double x, double y, double z) {
        double d = (double)this.getX() + 0.5 - x;
        double e = (double)this.getY() + 0.5 - y;
        double f = (double)this.getZ() + 0.5 - z;
        return d * d + e * e + f * f;
    }

    /**
     * {@return the squared distance between here and {@code (x, y, z)}}
     * This is equivalent to {@code Vec3d.of(this).squaredDistanceTo(x, y, z)}.
     */
    public double getSquaredDistance(double x, double y, double z) {
        double d = (double)this.getX() - x;
        double e = (double)this.getY() - y;
        double f = (double)this.getZ() - z;
        return d * d + e * e + f * f;
    }

    /**
     * {@return the Manhattan distance between here and {@code vec}}
     * 
     * <p>Manhattan distance, also called taxicab distance or snake distance, is the
     * distance measured as the sum of the absolute differences of their coordinates.
     * For example, the Manhattan distance between {@code (0, 0, 0)} and {@code (1, 1, 1)}
     * is {@code 3}.
     */
    public int getManhattanDistance(Vec3i vec) {
        float f = Math.abs(vec.getX() - this.getX());
        float g = Math.abs(vec.getY() - this.getY());
        float h = Math.abs(vec.getZ() - this.getZ());
        return (int)(f + g + h);
    }

    /**
     * {@return the component on the {@code axis} axis}
     */
    public int getComponentAlongAxis(Direction.Axis axis) {
        return axis.choose(this.x, this.y, this.z);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }

    /**
     * {@return the coordinates joined with a colon and a space}
     */
    public String toShortString() {
        return this.getX() + ", " + this.getY() + ", " + this.getZ();
    }
}

