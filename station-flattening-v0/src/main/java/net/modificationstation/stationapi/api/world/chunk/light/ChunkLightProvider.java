package net.modificationstation.stationapi.api.world.chunk.light;

import java.util.Arrays;

import net.minecraft.block.BlockBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.level.chunk.NibbleArray;
import net.modificationstation.stationapi.impl.util.math.ChunkPos;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

public abstract class ChunkLightProvider<M extends ChunkToNibbleArrayMap<M>, S extends LightStorage<M>>
extends LevelPropagator
implements ChunkLightingView {
    public static final long field_31708 = Long.MAX_VALUE;
    private static final Direction[] DIRECTIONS = Direction.values();
    protected final ChunkProvider chunkProvider;
    protected final LightType type;
    protected final S lightStorage;
    private boolean field_15794;
    protected final BlockPos.Mutable reusableBlockPos = new BlockPos.Mutable();
    private static final int field_31709 = 2;
    private final long[] cachedChunkPositions = new long[2];
    private final Chunk[] cachedChunks = new Chunk[2];

    public ChunkLightProvider(ChunkProvider chunkProvider, LightType type, S lightStorage) {
        super(16, 256, 8192);
        this.chunkProvider = chunkProvider;
        this.type = type;
        this.lightStorage = lightStorage;
        this.clearChunkCache();
    }

    @Override
    protected void resetLevel(long id) {
        ((LightStorage)this.lightStorage).updateAll();
        if (((LightStorage)this.lightStorage).hasSection(ChunkSectionPos.fromBlockPos(id))) {
            super.resetLevel(id);
        }
    }

    @Nullable
    private Chunk getChunk(int chunkX, int chunkZ) {
        long l = ChunkPos.toLong(chunkX, chunkZ);
        for (int i = 0; i < 2; ++i) {
            if (l != this.cachedChunkPositions[i]) continue;
            return this.cachedChunks[i];
        }
        Chunk chunk = this.chunkProvider.getChunk(chunkX, chunkZ);
        for (int j = 1; j > 0; --j) {
            this.cachedChunkPositions[j] = this.cachedChunkPositions[j - 1];
            this.cachedChunks[j] = this.cachedChunks[j - 1];
        }
        this.cachedChunkPositions[0] = l;
        this.cachedChunks[0] = chunk;
        return chunk;
    }

    private void clearChunkCache() {
        Arrays.fill(this.cachedChunkPositions, ChunkPos.MARKER);
        Arrays.fill(this.cachedChunks, null);
    }

    protected BlockState getStateForLighting(long pos, @Nullable MutableInt mutableInt) {
        if (pos == Long.MAX_VALUE) {
            if (mutableInt != null) {
                mutableInt.setValue(0);
            }
            return States.AIR.get();
        }
        int i = ChunkSectionPos.getSectionCoord(BlockPos.unpackLongX(pos));
        Chunk chunk = this.getChunk(i, ChunkSectionPos.getSectionCoord(BlockPos.unpackLongZ(pos)));
        if (chunk == null) {
            if (mutableInt != null) {
                mutableInt.setValue(16);
            }
            return BlockBase.BEDROCK.getDefaultState();
        }
        this.reusableBlockPos.set(pos);
        BlockState blockState = chunk.getBlockState(this.reusableBlockPos);
        boolean bl = blockState.isOpaque() && blockState.hasSidedTransparency();
        if (mutableInt != null) {
            mutableInt.setValue(blockState.getOpacity(this.chunkProvider.getWorld(), this.reusableBlockPos));
        }
        return bl ? blockState : States.AIR.get();
    }

    protected VoxelShape getOpaqueShape(BlockState world, long pos, Direction facing) {
        return world.isOpaque() ? world.getCullingFace(this.chunkProvider.getWorld(), this.reusableBlockPos.set(pos), facing) : VoxelShapes.empty();
    }

    public static int getRealisticOpacity(BlockView world, BlockState state1, BlockPos pos1, BlockState state2, BlockPos pos2, Direction direction, int opacity2) {
        VoxelShape voxelShape2;
        boolean bl2;
        boolean bl = state1.isOpaque() && state1.hasSidedTransparency();
        boolean bl3 = bl2 = state2.isOpaque() && state2.hasSidedTransparency();
        if (!bl && !bl2) {
            return opacity2;
        }
        VoxelShape voxelShape = bl ? state1.getCullingShape(world, pos1) : VoxelShapes.empty();
        VoxelShape voxelShape3 = voxelShape2 = bl2 ? state2.getCullingShape(world, pos2) : VoxelShapes.empty();
        if (VoxelShapes.adjacentSidesCoverSquare(voxelShape, voxelShape2, direction)) {
            return 16;
        }
        return opacity2;
    }

    @Override
    protected boolean isMarker(long id) {
        return id == Long.MAX_VALUE;
    }

    @Override
    protected int recalculateLevel(long id, long excludedId, int maxLevel) {
        return 0;
    }

    @Override
    protected int getLevel(long id) {
        if (id == Long.MAX_VALUE) {
            return 0;
        }
        return 15 - ((LightStorage)this.lightStorage).get(id);
    }

    protected int getCurrentLevelFromSection(NibbleArray section, long blockPos) {
        return 15 - section.get(ChunkSectionPos.getLocalCoord(BlockPos.unpackLongX(blockPos)), ChunkSectionPos.getLocalCoord(BlockPos.unpackLongY(blockPos)), ChunkSectionPos.getLocalCoord(BlockPos.unpackLongZ(blockPos)));
    }

    @Override
    protected void setLevel(long id, int level) {
        ((LightStorage)this.lightStorage).set(id, Math.min(15, 15 - level));
    }

    @Override
    protected int getPropagatedLevel(long sourceId, long targetId, int level) {
        return 0;
    }

    @Override
    public boolean hasUpdates() {
        return this.hasPendingUpdates() || ((LevelPropagator)this.lightStorage).hasPendingUpdates() || ((LightStorage)this.lightStorage).hasLightUpdates();
    }

    @Override
    public int doLightUpdates(int i, boolean doSkylight, boolean skipEdgeLightPropagation) {
        if (!this.field_15794) {
            if (((LevelPropagator)this.lightStorage).hasPendingUpdates() && (i = ((LevelPropagator)this.lightStorage).applyPendingUpdates(i)) == 0) {
                return i;
            }
            ((LightStorage)this.lightStorage).updateLight(this, doSkylight, skipEdgeLightPropagation);
        }
        this.field_15794 = true;
        if (this.hasPendingUpdates()) {
            i = this.applyPendingUpdates(i);
            this.clearChunkCache();
            if (i == 0) {
                return i;
            }
        }
        this.field_15794 = false;
        ((LightStorage)this.lightStorage).notifyChanges();
        return i;
    }

    protected void enqueueSectionData(long sectionPos, @Nullable NibbleArray lightArray, boolean nonEdge) {
        ((LightStorage)this.lightStorage).enqueueSectionData(sectionPos, lightArray, nonEdge);
    }

    @Override
    @Nullable
    public NibbleArray getLightSection(ChunkSectionPos pos) {
        return ((LightStorage)this.lightStorage).getLightSection(pos.asLong());
    }

    @Override
    public int getLightLevel(BlockPos pos) {
        return ((LightStorage)this.lightStorage).getLight(pos.asLong());
    }

    public String displaySectionLevel(long sectionPos) {
        return "" + ((LightStorage)this.lightStorage).getLevel(sectionPos);
    }

    @Override
    public void checkBlock(BlockPos pos) {
        long l = pos.asLong();
        this.resetLevel(l);
        for (Direction direction : DIRECTIONS) {
            this.resetLevel(BlockPos.offset(l, direction));
        }
    }

    @Override
    public void addLightSource(BlockPos pos, int level) {
    }

    @Override
    public void setSectionStatus(ChunkSectionPos pos, boolean notReady) {
        ((LightStorage)this.lightStorage).setSectionStatus(pos.asLong(), notReady);
    }

    @Override
    public void setColumnEnabled(ChunkPos pos, boolean retainData) {
        long l = ChunkSectionPos.withZeroY(ChunkSectionPos.asLong(pos.x, 0, pos.z));
        ((LightStorage)this.lightStorage).setColumnEnabled(l, retainData);
    }

    public void setRetainColumn(ChunkPos pos, boolean retainData) {
        long l = ChunkSectionPos.withZeroY(ChunkSectionPos.asLong(pos.x, 0, pos.z));
        ((LightStorage)this.lightStorage).setRetainColumn(l, retainData);
    }
}

