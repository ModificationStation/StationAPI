package net.modificationstation.stationapi.mixin.maths;

import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.util.BlockRotation;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3i;
import net.modificationstation.stationapi.api.util.math.StationBlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TilePos.class)
public class TilePosMixin implements StationBlockPos {
    @Shadow @Final public int x;

    @Shadow @Final public int y;

    @Shadow @Final public int z;

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public TilePos add(double x, double y, double z) {
        if (x == 0.0 && y == 0.0 && z == 0.0) {
            //noinspection DataFlowIssue
            return (TilePos) (Object) this;
        }
        return StationBlockPos.create((double) getX() + x, (double) getY() + y, (double) getZ() + z);
    }

    @Override
    public TilePos add(int x, int y, int z) {
        if (x == 0 && y == 0 && z == 0) {
            //noinspection DataFlowIssue
            return (TilePos) (Object) this;
        }
        return new TilePos(getX() + x, getY() + y, getZ() + z);
    }

    @Override
    public TilePos subtract(Vec3i vec3i) {
        return StationBlockPos.super.subtract(vec3i);
    }

    @Override
    public TilePos multiply(int i) {
        return switch (i) {
            case 1 -> //noinspection DataFlowIssue
                    (TilePos) (Object) this;
            case 0 -> ORIGIN;
            default -> new TilePos(getX() * i, getY() * i, getZ() * i);
        };
    }

    @Override
    public TilePos offset(Direction direction, int i) {
        //noinspection DataFlowIssue
        return i == 0 ?
                (TilePos) (Object) this :
                new TilePos(getX() + direction.getOffsetX() * i, getY() + direction.getOffsetY() * i, getZ() + direction.getOffsetZ() * i);
    }

    @Override
    public TilePos offset(Direction.Axis axis, int i) {
        if (i == 0) {
            //noinspection DataFlowIssue
            return (TilePos) (Object) this;
        }
        int x = axis == Direction.Axis.X ? i : 0;
        int y = axis == Direction.Axis.Y ? i : 0;
        int z = axis == Direction.Axis.Z ? i : 0;
        return new TilePos(getX() + x, getY() + y, getZ() + z);
    }

    @Override
    public TilePos rotate(BlockRotation rotation) {
        return switch (rotation) {
            default -> //noinspection DataFlowIssue
                    (TilePos) (Object) this;
            case CLOCKWISE_90 -> new TilePos(-this.getZ(), this.getY(), this.getX());
            case CLOCKWISE_180 -> new TilePos(-this.getX(), this.getY(), -this.getZ());
            case COUNTERCLOCKWISE_90 -> new TilePos(this.getZ(), this.getY(), -this.getX());
        };
    }

    @Override
    public TilePos withY(int y) {
        return StationBlockPos.super.withY(y);
    }

    @Override
    public TilePos toImmutable() {
        //noinspection DataFlowIssue
        return (TilePos) (Object) this;
    }
}
