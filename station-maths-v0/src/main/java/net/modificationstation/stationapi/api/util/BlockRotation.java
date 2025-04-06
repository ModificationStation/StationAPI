package net.modificationstation.stationapi.api.util;

import com.mojang.serialization.Codec;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.DirectionTransformation;

import java.util.List;
import java.util.Random;

public enum BlockRotation implements StringIdentifiable {
    NONE("none", DirectionTransformation.IDENTITY),
    CLOCKWISE_90("clockwise_90", DirectionTransformation.ROT_90_Y_NEG),
    CLOCKWISE_180("180", DirectionTransformation.ROT_180_FACE_XZ),
    COUNTERCLOCKWISE_90("counterclockwise_90", DirectionTransformation.ROT_90_Y_POS);

    public static final Codec<BlockRotation> CODEC;
    private final String id;
    private final DirectionTransformation directionTransformation;

    BlockRotation(String id, DirectionTransformation directionTransformation) {
        this.id = id;
        this.directionTransformation = directionTransformation;
    }

    public BlockRotation rotate(BlockRotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 -> switch (this) {
                case NONE -> CLOCKWISE_180;
                case CLOCKWISE_90 -> COUNTERCLOCKWISE_90;
                case CLOCKWISE_180 -> NONE;
                case COUNTERCLOCKWISE_90 -> CLOCKWISE_90;
            };
            case COUNTERCLOCKWISE_90 -> switch (this) {
                case NONE -> COUNTERCLOCKWISE_90;
                case CLOCKWISE_90 -> NONE;
                case CLOCKWISE_180 -> CLOCKWISE_90;
                case COUNTERCLOCKWISE_90 -> CLOCKWISE_180;
            };
            case CLOCKWISE_90 -> switch (this) {
                case NONE -> CLOCKWISE_90;
                case CLOCKWISE_90 -> CLOCKWISE_180;
                case CLOCKWISE_180 -> COUNTERCLOCKWISE_90;
                case COUNTERCLOCKWISE_90 -> NONE;
            };
            case NONE -> this;
        };
    }

    public DirectionTransformation getDirectionTransformation() {
        return this.directionTransformation;
    }

    public Direction rotate(Direction direction) {
        if (direction.getAxis() == Direction.Axis.Y) return direction;
        return switch (this) {
            case CLOCKWISE_180 -> direction.getOpposite();
            case COUNTERCLOCKWISE_90 -> direction.rotateYCounterclockwise();
            case CLOCKWISE_90 -> direction.rotateYClockwise();
            case NONE -> direction;
        };
    }

    public int rotate(int rotation, int fullTurn) {
        return switch (this) {
            case CLOCKWISE_180 -> (rotation + fullTurn / 2) % fullTurn;
            case COUNTERCLOCKWISE_90 -> (rotation + fullTurn * 3 / 4) % fullTurn;
            case CLOCKWISE_90 -> (rotation + fullTurn / 4) % fullTurn;
            case NONE -> rotation;
        };
    }

    public static BlockRotation random(Random random) {
        return Util.getRandom(BlockRotation.values(), random);
    }

    public static List<BlockRotation> randomRotationOrder(Random random) {
        return Util.copyShuffled(BlockRotation.values(), random);
    }

    @Override
    public String asString() {
        return this.id;
    }

    static {
        CODEC = StringIdentifiable.createCodec(BlockRotation::values);
    }
}

