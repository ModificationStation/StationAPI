package net.modificationstation.stationapi.api.util.math;

import net.modificationstation.stationapi.api.util.BlockRotation;
import net.modificationstation.stationapi.mixin.maths.TilePosAccessor;

import static net.minecraft.util.math.MathHelper.floor;

import net.minecraft.util.math.BlockPos;

public class MutableBlockPos extends BlockPos {
    private final TilePosAccessor _super = (TilePosAccessor) this;

    public MutableBlockPos() {
        this(0, 0, 0);
    }

    public MutableBlockPos(int i, int j, int k) {
        super(i, j, k);
    }

    public MutableBlockPos(double d, double e, double f) {
        this(floor(d), floor(e), floor(f));
    }

    @Override
    public BlockPos add(double d, double e, double f) {
        return super.add(d, e, f).toImmutable();
    }

    @Override
    public BlockPos add(int i, int j, int k) {
        return super.add(i, j, k).toImmutable();
    }

    @Override
    public BlockPos multiply(int i) {
        return super.multiply(i).toImmutable();
    }

    @Override
    public BlockPos offset(Direction direction, int i) {
        return super.offset(direction, i).toImmutable();
    }

    @Override
    public BlockPos offset(Direction.Axis axis, int i) {
        return super.offset(axis, i).toImmutable();
    }

    @Override
    public BlockPos rotate(BlockRotation rotation) {
        return super.rotate(rotation).toImmutable();
    }

    public MutableBlockPos set(int x, int y, int z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        return this;
    }

    public MutableBlockPos set(double x, double y, double z) {
        return this.set(floor(x), floor(y), floor(z));
    }

    public MutableBlockPos set(Vec3i pos) {
        return set(pos.getX(), pos.getY(), pos.getZ());
    }

    public MutableBlockPos set(long pos) {
        return set(StationBlockPos.unpackLongX(pos), StationBlockPos.unpackLongY(pos), StationBlockPos.unpackLongZ(pos));
    }

    public MutableBlockPos set(AxisCycleDirection axis, int x, int y, int z) {
        return set(axis.choose(x, y, z, Direction.Axis.X), axis.choose(x, y, z, Direction.Axis.Y), axis.choose(x, y, z, Direction.Axis.Z));
    }

    public MutableBlockPos set(BlockPos pos, Direction direction) {
        return set(pos.getX() + direction.getOffsetX(), pos.getY() + direction.getOffsetY(), pos.getZ() + direction.getOffsetZ());
    }

    public MutableBlockPos set(Vec3i pos, Direction direction) {
        return set(pos.getX() + direction.getOffsetX(), pos.getY() + direction.getOffsetY(), pos.getZ() + direction.getOffsetZ());
    }

    public MutableBlockPos set(Vec3i pos, int x, int y, int z) {
        return set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
    }

    public MutableBlockPos set(Vec3i vec1, Vec3i vec2) {
        return set(vec1.getX() + vec2.getX(), vec1.getY() + vec2.getY(), vec1.getZ() + vec2.getZ());
    }

    public MutableBlockPos move(Direction direction) {
        return move(direction, 1);
    }

    public MutableBlockPos move(Direction direction, int distance) {
        return set(
                getX() + direction.getOffsetX() * distance,
                getY() + direction.getOffsetY() * distance,
                getZ() + direction.getOffsetZ() * distance
        );
    }

    public MutableBlockPos move(int dx, int dy, int dz) {
        return set(
                getX() + dx,
                getY() + dy,
                getZ() + dz
        );
    }

    public MutableBlockPos move(Vec3i vec) {
        return set(
                getX() + vec.getX(),
                getY() + vec.getY(),
                getZ() + vec.getZ()
        );
    }

    public MutableBlockPos clamp(Direction.Axis axis, int min, int max) {
        return switch (axis) {
            case X -> this.set(MathHelper.clamp(this.getX(), min, max), this.getY(), this.getZ());
            case Y -> this.set(this.getX(), MathHelper.clamp(this.getY(), min, max), this.getZ());
            case Z -> this.set(this.getX(), this.getY(), MathHelper.clamp(this.getZ(), min, max));
        };
    }

    public MutableBlockPos setX(int i) {
        _super.stationapi_setX(i);
        return this;
    }

    public MutableBlockPos setY(int i) {
        _super.stationapi_setY(i);
        return this;
    }

    public MutableBlockPos setZ(int i) {
        _super.stationapi_setZ(i);
        return this;
    }

    @Override
    public BlockPos toImmutable() {
        return new BlockPos(getX(), getY(), getZ());
    }
}
