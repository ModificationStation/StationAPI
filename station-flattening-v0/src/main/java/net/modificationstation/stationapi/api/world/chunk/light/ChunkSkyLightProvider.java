package net.modificationstation.stationapi.api.world.chunk.light;

import java.util.Locale;

import net.minecraft.level.LightType;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import org.apache.commons.lang3.mutable.MutableInt;

public final class ChunkSkyLightProvider
extends ChunkLightProvider<SkyLightStorage.Data, SkyLightStorage> {
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};

    public ChunkSkyLightProvider(ChunkProvider chunkProvider) {
        super(chunkProvider, LightType.SKY, new SkyLightStorage(chunkProvider));
    }

    @Override
    protected int getPropagatedLevel(long sourceId, long targetId, int level) {
        boolean bl2;
        VoxelShape voxelShape2;
        int q;
        int p;
        if (targetId == Long.MAX_VALUE || sourceId == Long.MAX_VALUE) {
            return 15;
        }
        if (level >= 15) {
            return level;
        }
        MutableInt mutableInt = new MutableInt();
        BlockState blockState = this.getStateForLighting(targetId, mutableInt);
        if (mutableInt.getValue() >= 15) {
            return 15;
        }
        int i = BlockPos.unpackLongX(sourceId);
        int j = BlockPos.unpackLongY(sourceId);
        int k = BlockPos.unpackLongZ(sourceId);
        int l = BlockPos.unpackLongX(targetId);
        int m = BlockPos.unpackLongY(targetId);
        int n = BlockPos.unpackLongZ(targetId);
        int o = Integer.signum(l - i);
        Direction direction = Direction.fromVector(o, p = Integer.signum(m - j), q = Integer.signum(n - k));
        if (direction == null) {
            throw new IllegalStateException(String.format(Locale.ROOT, "Light was spread in illegal direction %d, %d, %d", o, p, q));
        }
        BlockState blockState2 = this.getStateForLighting(sourceId, null);
        VoxelShape voxelShape = this.getOpaqueShape(blockState2, sourceId, direction);
        if (VoxelShapes.unionCoversFullCube(voxelShape, voxelShape2 = this.getOpaqueShape(blockState, targetId, direction.getOpposite()))) {
            return 15;
        }
        boolean bl = i == l && k == n;
        boolean bl3 = bl2 = bl && j > m;
        if (bl2 && level == 0 && mutableInt.getValue() == 0) {
            return 0;
        }
        return level + Math.max(1, mutableInt.getValue());
    }

    @Override
    protected void propagateLevel(long id, int level, boolean decrease) {
        long q;
        long r;
        int m;
        long l = ChunkSectionPos.fromBlockPos(id);
        int i = BlockPos.unpackLongY(id);
        int j = ChunkSectionPos.getLocalCoord(i);
        int k = ChunkSectionPos.getSectionCoord(i);
        if (j != 0) {
            m = 0;
        } else {
            int n = 0;
            while (!((SkyLightStorage)this.lightStorage).hasSection(ChunkSectionPos.offset(l, 0, -n - 1, 0)) && ((SkyLightStorage)this.lightStorage).isAboveMinHeight(k - n - 1)) {
                ++n;
            }
            m = n;
        }
        long o = BlockPos.add(id, 0, -1 - m * 16, 0);
        long p = ChunkSectionPos.fromBlockPos(o);
        if (l == p || ((SkyLightStorage)this.lightStorage).hasSection(p)) {
            this.propagateLevel(id, o, level, decrease);
        }
        if (l == (r = ChunkSectionPos.fromBlockPos(q = BlockPos.offset(id, Direction.UP))) || ((SkyLightStorage)this.lightStorage).hasSection(r)) {
            this.propagateLevel(id, q, level, decrease);
        }
        block1: for (Direction direction : HORIZONTAL_DIRECTIONS) {
            int s = 0;
            do {
                long t;
                long u;
                if (l == (u = ChunkSectionPos.fromBlockPos(t = BlockPos.add(id, direction.getOffsetX(), -s, direction.getOffsetZ())))) {
                    this.propagateLevel(id, t, level, decrease);
                    continue block1;
                }
                if (!((SkyLightStorage)this.lightStorage).hasSection(u)) continue;
                long v = BlockPos.add(id, 0, -s, 0);
                this.propagateLevel(v, t, level, decrease);
            } while (++s <= m * 16);
        }
    }

    @Override
    protected int recalculateLevel(long id, long excludedId, int maxLevel) {
        int i = maxLevel;
        long l = ChunkSectionPos.fromBlockPos(id);
        ChunkNibbleArray chunkNibbleArray = ((SkyLightStorage)this.lightStorage).getLightSection(l, true);
        for (Direction direction : DIRECTIONS) {
            int j;
            long m = BlockPos.offset(id, direction);
            if (m == excludedId) continue;
            long n = ChunkSectionPos.fromBlockPos(m);
            ChunkNibbleArray chunkNibbleArray2 = l == n ? chunkNibbleArray : ((SkyLightStorage)this.lightStorage).getLightSection(n, true);
            if (chunkNibbleArray2 != null) {
                j = this.getCurrentLevelFromSection(chunkNibbleArray2, m);
            } else {
                if (direction == Direction.DOWN) continue;
                j = 15 - ((SkyLightStorage)this.lightStorage).getLight(m, true);
            }
            int k = this.getPropagatedLevel(m, id, j);
            if (i > k) {
                i = k;
            }
            if (i != 0) continue;
            return i;
        }
        return i;
    }

    @Override
    protected void resetLevel(long id) {
        ((SkyLightStorage)this.lightStorage).updateAll();
        long l = ChunkSectionPos.fromBlockPos(id);
        if (((SkyLightStorage)this.lightStorage).hasSection(l)) {
            super.resetLevel(id);
        } else {
            id = BlockPos.removeChunkSectionLocalY(id);
            while (!((SkyLightStorage)this.lightStorage).hasSection(l) && !((SkyLightStorage)this.lightStorage).isAtOrAboveTopmostSection(l)) {
                l = ChunkSectionPos.offset(l, Direction.UP);
                id = BlockPos.add(id, 0, 16, 0);
            }
            if (((SkyLightStorage)this.lightStorage).hasSection(l)) {
                super.resetLevel(id);
            }
        }
    }

    @Override
    public String displaySectionLevel(long sectionPos) {
        return super.displaySectionLevel(sectionPos) + (((SkyLightStorage)this.lightStorage).isAtOrAboveTopmostSection(sectionPos) ? "*" : "");
    }
}

