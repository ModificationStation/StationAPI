package net.modificationstation.stationapi.api.util.math;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.gson.annotations.SerializedName;
import com.mojang.serialization.Codec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.maths.Vec3i;
import net.modificationstation.stationapi.api.util.StringIdentifiable;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.util.math.Direction.Axis.*;

@RequiredArgsConstructor
public enum Direction implements StringIdentifiable {

    @SerializedName("down")
    DOWN("down", AxisDirection.NEGATIVE, Y, new Vec3i(0, -1, 0)),
    @SerializedName("up")
    UP("up", AxisDirection.POSITIVE, Y, new Vec3i(0, 1, 0)),
    @SerializedName("east")
    EAST("east", AxisDirection.NEGATIVE, Z, new Vec3i(0, 0, -1)),
    @SerializedName("west")
    WEST("west", AxisDirection.POSITIVE, Z, new Vec3i(0, 0, 1)),
    @SerializedName("north")
    NORTH("north", AxisDirection.NEGATIVE, X, new Vec3i(-1, 0, 0)),
    @SerializedName("south")
    SOUTH("south", AxisDirection.POSITIVE, X, new Vec3i(1, 0, 0));

    private static final Direction[] ALL = values();
    private static final ImmutableMap<String, Direction> NAME_LOOKUP = Util.createLookupBy(direction -> direction.name, ALL);

    static {
        DOWN.opposite = UP;
        UP.opposite = DOWN;
        EAST.opposite = WEST;
        WEST.opposite = EAST;
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
    }

    private final String name;
    public final AxisDirection direction;
    public final Axis axis;
    public final Vec3i vector;
    @Getter
    private Direction opposite;

    public Quaternion getRotationQuaternion() {
        Quaternion quaternion = Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f);
        return switch (this) {
            case DOWN -> Vec3f.POSITIVE_X.getDegreesQuaternion(180.0f);
            case UP -> Quaternion.IDENTITY.copy();
            case EAST -> {
                quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f));
                yield quaternion;
            }
            case WEST -> quaternion;
            case NORTH -> {
                quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0f));
                yield quaternion;
            }
            case SOUTH -> {
                quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0f));
                yield quaternion;
            }
        };
    }

    public int getId() {
        return this.ordinal();
    }

    public static Direction from(Axis axis, AxisDirection direction) {
        return switch (axis) {
            case X -> direction == AxisDirection.NEGATIVE ? NORTH : SOUTH;
            case Y -> direction == AxisDirection.NEGATIVE ? DOWN : UP;
            case Z -> direction == AxisDirection.NEGATIVE ? EAST : WEST;
        };
    }

    public static Direction getFacing(float x, float y, float z) {
        Direction direction = NORTH;
        float f = Float.MIN_VALUE;

        for (Direction direction2 : ALL) {
            float g = x * (float) direction2.vector.x + y * (float) direction2.vector.y + z * (float) direction2.vector.z;
            if (g > f) {
                f = g;
                direction = direction2;
            }
        }

        return direction;
    }

    public static Direction transform(Matrix4f matrix, Direction direction) {
        Vec3i vec3i = direction.vector;
        Vector4f vector4f = new Vector4f((float)vec3i.x, (float)vec3i.y, (float)vec3i.z, 0.0F);
        vector4f.transform(matrix);
        return getFacing(vector4f.getX(), vector4f.getY(), vector4f.getZ());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String asString() {
        return toString();
    }

    public static Direction byName(String name) {
        return NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }

    public enum Type implements Iterable<Direction>, Predicate<Direction> {
        HORIZONTAL(new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}, new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}),
        VERTICAL(new Direction[]{Direction.UP, Direction.DOWN}, new Direction.Axis[]{Direction.Axis.Y});

        private final Direction[] facingArray;
        private final Direction.Axis[] axisArray;

        Type(Direction[] facingArray, Direction.Axis[] axisArray) {
            this.facingArray = facingArray;
            this.axisArray = axisArray;
        }

        public Direction random(Random random) {
            return (Direction)Util.getRandom((Object[])this.facingArray, random);
        }

        public Direction.Axis randomAxis(Random random) {
            return (Direction.Axis)Util.getRandom((Object[])this.axisArray, random);
        }

        @Override
        public boolean test(@Nullable Direction direction) {
            return direction != null && direction.axis.getType() == this;
        }

        @Override
        public @NotNull Iterator<Direction> iterator() {
            return Iterators.forArray(this.facingArray);
        }

        public Stream<Direction> stream() {
            return Arrays.stream(this.facingArray);
        }
    }

    public enum AxisDirection {
        POSITIVE(1, "Towards positive"),
        NEGATIVE(-1, "Towards negative");

        private final int offset;
        private final String description;

        AxisDirection(int offset, String description) {
            this.offset = offset;
            this.description = description;
        }

        public int offset() {
            return this.offset;
        }

        public String toString() {
            return this.description;
        }

        public Direction.AxisDirection getOpposite() {
            return this == POSITIVE ? NEGATIVE : POSITIVE;
        }
    }

    @RequiredArgsConstructor
    public enum Axis implements StringIdentifiable {

        X("x") {

            @Override
            public int choose(int x, int y, int z) {
                return x;
            }

            @Override
            public double choose(double x, double y, double z) {
                return x;
            }

            @Override
            public Type getType() {
                return Type.HORIZONTAL;
            }
        },
        Y("y") {

            @Override
            public int choose(int x, int y, int z) {
                return y;
            }

            @Override
            public double choose(double x, double y, double z) {
                return y;
            }

            @Override
            public Type getType() {
                return Type.VERTICAL;
            }
        },
        Z("z") {

            @Override
            public int choose(int x, int y, int z) {
                return z;
            }

            @Override
            public double choose(double x, double y, double z) {
                return z;
            }

            @Override
            public Type getType() {
                return Type.HORIZONTAL;
            }
        };

        public static final Codec<Axis> CODEC = StringIdentifiable.createCodec(Direction.Axis::values, Direction.Axis::fromName);

        private static final ImmutableMap<String, Axis> NAME_LOOKUP = Util.createLookupBy(axis -> axis.name, Axis.values());

        private final String name;

        @Nullable
        public static Direction.Axis fromName(String name) {
            return NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
        }

        public abstract int choose(int x, int y, int z);

        public abstract double choose(double var1, double var3, double var5);

        public abstract Direction.Type getType();

        @Override
        public String toString() {
            return name;
        }

        @Override
        public String asString() {
            return name;
        }
    }
}
