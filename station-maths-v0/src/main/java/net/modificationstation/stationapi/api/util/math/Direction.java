package net.modificationstation.stationapi.api.util.math;

import com.google.common.collect.Iterators;
import com.google.gson.annotations.SerializedName;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.StringIdentifiable;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.util.math.Direction.Axis.*;

@RequiredArgsConstructor
public enum Direction implements StringIdentifiable {
    @SerializedName("down")
    DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, Y, new Vec3i(0, -1, 0)),
    @SerializedName("up")
    UP(1, 0, -1, "up", AxisDirection.POSITIVE, Y, new Vec3i(0, 1, 0)),
    @SerializedName("east")
    EAST(2, 3, 2, "east", AxisDirection.NEGATIVE, Z, new Vec3i(0, 0, -1)),
    @SerializedName("west")
    WEST(3, 2, 0, "west", AxisDirection.POSITIVE, Z, new Vec3i(0, 0, 1)),
    @SerializedName("north")
    NORTH(4, 5, 1, "north", AxisDirection.NEGATIVE, X, new Vec3i(-1, 0, 0)),
    @SerializedName("south")
    SOUTH(5, 4, 3, "south", AxisDirection.POSITIVE, X, new Vec3i(1, 0, 0));

    public static final Codec<Direction> CODEC;
    public static final com.mojang.serialization.Codec<Direction> VERTICAL_CODEC;
    private final int id;
    private final int idOpposite;
    private final int idHorizontal;
    private final String name;
    private final Axis axis;
    private final AxisDirection direction;
    private final Vec3i vector;
    private final int offsetX, offsetY, offsetZ;
    private static final Direction[] ALL;
    private static final Direction[] VALUES;
    private static final Direction[] HORIZONTAL;
    private static final Long2ObjectMap<Direction> VECTOR_TO_DIRECTION;

    Direction(int id, int idOpposite, int idHorizontal, String name, AxisDirection direction, Axis axis, Vec3i vector) {
        this.id = id;
        this.idHorizontal = idHorizontal;
        this.idOpposite = idOpposite;
        this.name = name;
        this.axis = axis;
        this.direction = direction;
        this.vector = vector;
        offsetX = vector.getX();
        offsetY = vector.getY();
        offsetZ = vector.getZ();
    }

    public static Direction[] getEntityFacingOrder(Entity entity) {
        final float f = entity.pitch * ((float)Math.PI / 180);
        final float g = -entity.yaw * ((float)Math.PI / 180);
        final float h = net.minecraft.util.math.MathHelper.sin(f);
        final float i = net.minecraft.util.math.MathHelper.cos(f);
        final float j = net.minecraft.util.math.MathHelper.sin(g);
        final float k = net.minecraft.util.math.MathHelper.cos(g);
        final boolean bl = j > 0.0f;
        final boolean bl2 = h < 0.0f;
        final boolean bl3 = k > 0.0f;
        final float l = bl ? j : -j;
        final float m = bl2 ? -h : h;
        final float n = bl3 ? k : -k;
        final float o = l * i;
        final float p = n * i;
        final Direction direction = bl ? SOUTH : NORTH;
        final Direction direction2 = bl2 ? UP : DOWN;
        final Direction direction3 = bl3 ? WEST : EAST;
        return l > n ? m > o ? Direction.listClosest(direction2, direction, direction3) : p > m ? Direction.listClosest(direction, direction3, direction2) : Direction.listClosest(direction, direction2, direction3) : m > p ? Direction.listClosest(direction2, direction3, direction) : o > m ? Direction.listClosest(direction3, direction, direction2) : Direction.listClosest(direction3, direction2, direction);
    }

    /**
     * Helper function that returns the 3 directions given, followed by the 3 opposite given in opposite order.
     */
    private static Direction[] listClosest(Direction first, Direction second, Direction third) {
        return new Direction[]{first, second, third, third.getOpposite(), second.getOpposite(), first.getOpposite()};
    }

    public static Direction transform(Matrix4f matrix, Direction direction) {
        Vec3i vec3i = direction.getVector();
        Vec4f vector4f = new Vec4f(vec3i.getX(), vec3i.getY(), vec3i.getZ(), 0.0f);
        vector4f.transform(matrix);
        return Direction.getFacing(vector4f.getX(), vector4f.getY(), vector4f.getZ());
    }

    public static Collection<Direction> shuffle(Random random) {
        return Util.copyShuffled(Direction.values(), random);
    }

    public static Stream<Direction> stream() {
        return Stream.of(ALL);
    }

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
                quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0f));
                yield quaternion;
            }
            case SOUTH -> {
                quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0f));
                yield quaternion;
            }
        };
    }

    public int getId() {
        return id;
    }

    public int getHorizontal() {
        return idHorizontal;
    }

    public AxisDirection getDirection() {
        return direction;
    }

    public static Direction getLookDirectionForAxis(Entity entity, Axis axis) {
        return switch (axis) {
            case X -> NORTH.pointsTo(entity.yaw) ? NORTH : SOUTH;
            case Z -> EAST.pointsTo(entity.yaw) ? EAST : WEST;
            case Y -> entity.pitch < 0.0f ? UP : DOWN;
        };
    }

    public Direction getOpposite() {
        return ALL[idOpposite];
    }

    public Direction rotateClockwise(Axis axis) {
        return switch (axis) {
            case X -> this == NORTH || this == SOUTH ? this : rotateXClockwise();
            case Y -> this == UP || this == DOWN ? this : rotateYClockwise();
            case Z -> this == EAST || this == WEST ? this : rotateZClockwise();
        };
    }

    public Direction rotateCounterclockwise(Axis axis) {
        return switch (axis) {
            case X -> this == NORTH || this == SOUTH ? this : rotateXCounterclockwise();
            case Y -> this == UP || this == DOWN ? this : rotateYCounterclockwise();
            case Z -> this == EAST || this == WEST ? this : rotateZCounterclockwise();
        };
    }

    public Direction rotateYClockwise() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            default -> throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        };
    }

    private Direction rotateXClockwise() {
        return switch (this) {
            case UP -> EAST;
            case EAST -> DOWN;
            case DOWN -> WEST;
            case WEST -> UP;
            default -> throw new IllegalStateException("Unable to get X-rotated facing of " + this);
        };
    }

    private Direction rotateXCounterclockwise() {
        return switch (this) {
            case UP -> WEST;
            case WEST -> DOWN;
            case DOWN -> EAST;
            case EAST -> UP;
            default -> throw new IllegalStateException("Unable to get X-rotated facing of " + this);
        };
    }

    private Direction rotateZClockwise() {
        return switch (this) {
            case UP -> NORTH;
            case NORTH -> DOWN;
            case DOWN -> SOUTH;
            case SOUTH -> UP;
            default -> throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
        };
    }

    private Direction rotateZCounterclockwise() {
        return switch (this) {
            case UP -> SOUTH;
            case SOUTH -> DOWN;
            case DOWN -> NORTH;
            case NORTH -> UP;
            default -> throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
        };
    }

    public Direction rotateYCounterclockwise() {
        return switch (this) {
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
            default -> throw new IllegalStateException("Unable to get CCW facing of " + this);
        };
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getOffsetZ() {
        return offsetZ;
    }

    public Vec3f getUnitVector() {
        return new Vec3f(getOffsetX(), getOffsetY(), getOffsetZ());
    }

    public String getName() {
        return name;
    }

    public Axis getAxis() {
        return axis;
    }

    @Nullable
    public static Direction byName(@Nullable String name) {
        return CODEC.byId(name);
    }

    public static Direction byId(int id) {
        return VALUES[Math.abs(id % VALUES.length)];
    }

    public static Direction fromHorizontal(int value) {
        return HORIZONTAL[Math.abs(value % HORIZONTAL.length)];
    }

    @Nullable
    public static Direction fromVector(BlockPos pos) {
        return VECTOR_TO_DIRECTION.get(pos.asLong());
    }

    @Nullable
    public static Direction fromVector(int x, int y, int z) {
        return VECTOR_TO_DIRECTION.get(StationBlockPos.asLong(x, y, z));
    }

    public static Direction fromRotation(double rotation) {
        return Direction.fromHorizontal(net.minecraft.util.math.MathHelper.floor(rotation / 90.0 + 0.5) & 3);
    }

    public static Direction from(Axis axis, AxisDirection direction) {
        return switch (axis) {
            case X -> direction == AxisDirection.POSITIVE ? SOUTH : NORTH;
            case Y -> direction == AxisDirection.POSITIVE ? UP : DOWN;
            case Z -> direction == AxisDirection.POSITIVE ? WEST : EAST;
        };
    }

    public float asRotation() {
        return (idHorizontal & 3) * 90;
    }

    public static Direction random(Random random) {
        return ALL[random.nextInt(ALL.length)];
    }

    public static Direction getFacing(double x, double y, double z) {
        return Direction.getFacing((float)x, (float)y, (float)z);
    }

    public static Direction getFacing(float x, float y, float z) {
        Direction direction = NORTH;
        float f = Float.MIN_VALUE;
        for (Direction direction2 : ALL) {
            float g = x * (float)direction2.vector.getX() + y * (float)direction2.vector.getY() + z * (float)direction2.vector.getZ();
            if (!(g > f)) continue;
            f = g;
            direction = direction2;
        }
        return direction;
    }

    public String toString() {
        return name;
    }

    @Override
    public String asString() {
        return name;
    }

    private static DataResult<Direction> validateVertical(Direction direction) {
        return direction.getAxis().isVertical() ? DataResult.success(direction) : DataResult.error(() -> "Expected a vertical direction");
    }

    public static Direction get(AxisDirection direction, Axis axis) {
        for (Direction direction2 : ALL) {
            if (direction2.getDirection() != direction || direction2.getAxis() != axis) continue;
            return direction2;
        }
        throw new IllegalArgumentException("No such direction: " + direction + " " + axis);
    }

    public Vec3i getVector() {
        return vector;
    }

    /**
     * {@return whether the given yaw points to the direction}
     *
     * @implNote This returns whether the yaw can make an acute angle with the direction.
     *
     * <p>This always returns {@code false} for vertical directions.
     */
    public boolean pointsTo(float yaw) {
        float f = yaw * ((float)Math.PI / 180);
        float g = -net.minecraft.util.math.MathHelper.sin(f);
        float h = net.minecraft.util.math.MathHelper.cos(f);
        return (float)vector.getX() * g + (float)vector.getZ() * h > 0.0f;
    }

    static {
        CODEC = StringIdentifiable.createCodec(Direction::values);
        VERTICAL_CODEC = CODEC.flatXmap(Direction::validateVertical, Direction::validateVertical);
        ALL = Direction.values();
        VALUES = Arrays.stream(ALL).sorted(Comparator.comparingInt(direction -> direction.id)).toArray(Direction[]::new);
        HORIZONTAL = Arrays.stream(ALL).filter(direction -> direction.getAxis().isHorizontal()).sorted(Comparator.comparingInt(direction -> direction.idHorizontal)).toArray(Direction[]::new);
        VECTOR_TO_DIRECTION = Arrays.stream(ALL).collect(Collectors.toMap(direction -> StationBlockPos.create(direction.getVector()).asLong(), direction -> direction, (direction1, direction2) -> {
            throw new IllegalArgumentException("Duplicate keys");
        }, Long2ObjectOpenHashMap::new));
    }

    public enum Axis implements StringIdentifiable,
            Predicate<Direction>
    {
        X("x"){

            @Override
            public int choose(int x, int y, int z) {
                return x;
            }

            @Override
            public double choose(double x, double y, double z) {
                return x;
            }
        },
        Y("y"){

            @Override
            public int choose(int x, int y, int z) {
                return y;
            }

            @Override
            public double choose(double x, double y, double z) {
                return y;
            }
        },
        Z("z"){

            @Override
            public int choose(int x, int y, int z) {
                return z;
            }

            @Override
            public double choose(double x, double y, double z) {
                return z;
            }
        };

        public static final Axis[] VALUES;
        public static final StringIdentifiable.Codec<Axis> CODEC;
        private final String name;

        Axis(String name) {
            this.name = name;
        }

        @Nullable
        public static Axis fromName(String name) {
            return CODEC.byId(name);
        }

        public String getName() {
            return name;
        }

        public boolean isVertical() {
            return this == Y;
        }

        public boolean isHorizontal() {
            return this == X || this == Z;
        }

        public String toString() {
            return name;
        }

        public static Axis pickRandomAxis(Random random) {
            return Util.getRandom(VALUES, random);
        }

        @Override
        public boolean test(@Nullable Direction direction) {
            return direction != null && direction.getAxis() == this;
        }

        public Type getType() {
            return switch (this) {
                case X, Z -> Type.HORIZONTAL;
                case Y -> Type.VERTICAL;
            };
        }

        @Override
        public String asString() {
            return name;
        }

        public abstract int choose(int var1, int var2, int var3);

        public abstract double choose(double var1, double var3, double var5);

        static {
            VALUES = Axis.values();
            CODEC = StringIdentifiable.createCodec(Axis::values);
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
            return offset;
        }

        public String getDescription() {
            return description;
        }

        public String toString() {
            return description;
        }

        public AxisDirection getOpposite() {
            return this == POSITIVE ? NEGATIVE : POSITIVE;
        }
    }

    public enum Type implements Iterable<Direction>,
            Predicate<Direction>
    {
        HORIZONTAL(new Direction[]{NORTH, EAST, SOUTH, WEST}, new Axis[]{Axis.X, Axis.Z}),
        VERTICAL(new Direction[]{UP, DOWN}, new Axis[]{Axis.Y});

        private final Direction[] facingArray;
        private final Axis[] axisArray;

        Type(Direction[] facingArray, Axis[] axisArray) {
            this.facingArray = facingArray;
            this.axisArray = axisArray;
        }

        public Direction random(Random random) {
            return Util.getRandom(facingArray, random);
        }

        public Axis randomAxis(Random random) {
            return Util.getRandom(axisArray, random);
        }

        @Override
        public boolean test(@Nullable Direction direction) {
            return direction != null && direction.getAxis().getType() == this;
        }

        @Override
        public @NotNull Iterator<Direction> iterator() {
            return Iterators.forArray(facingArray);
        }

        public Stream<Direction> stream() {
            return Arrays.stream(facingArray);
        }

        public List<Direction> getShuffled(Random random) {
            return Util.copyShuffled(facingArray, random);
        }
    }
}
