package net.modificationstation.stationapi.api.world.chunk;

import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.world.BlockStateView;

public interface StationFlatteningChunk extends BlockStateView {

    @Override
    default BlockState getBlockState(int x, int y, int z) {
        return Util.assertImpl();
    }

    /**
     * Sets the block state, replacing the block only if it changes.
     *
     * <p>If the block stays the same, the state is changed in place: {@code onStateReplaced} and
     * {@code onBlockPlaced} aren't called, so block entities are kept, and metadata is kept too.
     * If the block changes, it's replaced like {@link net.minecraft.world.chunk.Chunk#setBlock(int, int, int, int)}
     * does, with the hooks in place of {@code onBreak} and {@code onPlaced}, and metadata reset to 0.
     *
     * @return the previous block state, or {@code null} if nothing changed
     */
    default BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        return Util.assertImpl();
    }

    /**
     * Sets the block state and metadata, replacing the block only if it changes.
     *
     * <p>If the block stays the same, both are changed in place: {@code onStateReplaced} and
     * {@code onBlockPlaced} aren't called, so block entities are kept.
     * If the block changes, it's replaced like {@link #setBlock(int, int, int, BlockState, int)}.
     *
     * @return the previous block state, or {@code null} if nothing changed
     */
    default BlockState setBlockState(int x, int y, int z, BlockState blockState, int meta) {
        return Util.assertImpl();
    }

    /**
     * The block state equivalent of {@link net.minecraft.world.chunk.Chunk#setBlock(int, int, int, int, int)}.
     *
     * <p>Like vanilla, any change to the state or metadata replaces the block, even if it stays the same,
     * so block entities are recreated. {@code onStateReplaced} and {@code onBlockPlaced} are called
     * where vanilla calls {@code onBreak} and {@code onPlaced}.
     *
     * @return the previous block state, or {@code null} if nothing changed
     */
    default BlockState setBlock(int x, int y, int z, BlockState blockState, int meta) {
        return Util.assertImpl();
    }
}
