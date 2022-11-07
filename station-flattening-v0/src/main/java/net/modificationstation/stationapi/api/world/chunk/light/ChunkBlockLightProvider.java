package net.modificationstation.stationapi.api.world.chunk.light;

import net.minecraft.level.LightType;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import org.apache.commons.lang3.mutable.MutableInt;

public final class ChunkBlockLightProvider
extends ChunkLightProvider<BlockLightStorage.Data, BlockLightStorage> {
    private static final Direction[] DIRECTIONS = Direction.values();
    private final BlockPos.Mutable mutablePos = new BlockPos.Mutable();

    public ChunkBlockLightProvider(ChunkProvider chunkProvider) {
        super(chunkProvider, LightType.field_2758, new BlockLightStorage(chunkProvider));
    }

    private int getLightSourceLuminance(long blockPos) {
        int i = BlockPos.unpackLongX(blockPos);
        int j = BlockPos.unpackLongY(blockPos);
        int k = BlockPos.unpackLongZ(blockPos);
        BlockView blockView = this.chunkProvider.getChunk(ChunkSectionPos.getSectionCoord(i), ChunkSectionPos.getSectionCoord(k));
        if (blockView != null) {
            return blockView.getLuminance(this.mutablePos.set(i, j, k));
        }
        return 0;
    }

    @Override
    protected int getPropagatedLevel(long sourceId, long targetId, int level) {
        VoxelShape voxelShape2;
        int k;
        int j;
        if (targetId == Long.MAX_VALUE) {
            return 15;
        }
        if (sourceId == Long.MAX_VALUE) {
            return level + 15 - this.getLightSourceLuminance(targetId);
        }
        if (level >= 15) {
            return level;
        }
        int i = Integer.signum(BlockPos.unpackLongX(targetId) - BlockPos.unpackLongX(sourceId));
        Direction direction = Direction.fromVector(i, j = Integer.signum(BlockPos.unpackLongY(targetId) - BlockPos.unpackLongY(sourceId)), k = Integer.signum(BlockPos.unpackLongZ(targetId) - BlockPos.unpackLongZ(sourceId)));
        if (direction == null) {
            return 15;
        }
        MutableInt mutableInt = new MutableInt();
        BlockState blockState = this.getStateForLighting(targetId, mutableInt);
        if (mutableInt.getValue() >= 15) {
            return 15;
        }
        BlockState blockState2 = this.getStateForLighting(sourceId, null);
        VoxelShape voxelShape = this.getOpaqueShape(blockState2, sourceId, direction);
        if (VoxelShapes.unionCoversFullCube(voxelShape, voxelShape2 = this.getOpaqueShape(blockState, targetId, direction.getOpposite()))) {
            return 15;
        }
        return level + Math.max(1, mutableInt.getValue());
    }

    @Override
    protected void propagateLevel(long id, int level, boolean decrease) {
        long l = ChunkSectionPos.fromBlockPos(id);
        for (Direction direction : DIRECTIONS) {
            long m = BlockPos.offset(id, direction);
            long n = ChunkSectionPos.fromBlockPos(m);
            if (l != n && !((BlockLightStorage)this.lightStorage).hasSection(n)) continue;
            this.propagateLevel(id, m, level, decrease);
        }
    }

    @Override
    protected int recalculateLevel(long id, long excludedId, int maxLevel) {
        int i = maxLevel;
        if (Long.MAX_VALUE != excludedId) {
            int j = this.getPropagatedLevel(Long.MAX_VALUE, id, 0);
            if (i > j) {
                i = j;
            }
            if (i == 0) {
                return i;
            }
        }
        long l = ChunkSectionPos.fromBlockPos(id);
        ChunkNibbleArray chunkNibbleArray = ((BlockLightStorage)this.lightStorage).getLightSection(l, true);
        for (Direction direction : DIRECTIONS) {
            long n;
            ChunkNibbleArray chunkNibbleArray2;
            long m = BlockPos.offset(id, direction);
            if (m == excludedId || (chunkNibbleArray2 = l == (n = ChunkSectionPos.fromBlockPos(m)) ? chunkNibbleArray : ((BlockLightStorage)this.lightStorage).getLightSection(n, true)) == null) continue;
            int k = this.getPropagatedLevel(m, id, this.getCurrentLevelFromSection(chunkNibbleArray2, m));
            if (i > k) {
                i = k;
            }
            if (i != 0) continue;
            return i;
        }
        return i;
    }

    @Override
    public void addLightSource(BlockPos pos, int level) {
        ((BlockLightStorage)this.lightStorage).updateAll();
        this.updateLevel(Long.MAX_VALUE, pos.asLong(), 15 - level, true);
    }
}

