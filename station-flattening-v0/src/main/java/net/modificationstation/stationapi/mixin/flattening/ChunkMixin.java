package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.world.chunk.StationFlatteningChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Chunk.class)
abstract class ChunkMixin implements StationFlatteningChunk {
    @Shadow public abstract int getBlockId(int i, int j, int k);

    @Shadow public abstract boolean setBlockId(int i, int j, int k, int l);

    @Shadow public abstract boolean setBlock(int i, int j, int k, int l, int m);

    @Override
    @Unique
    public BlockState getBlockState(int x, int y, int z) {
        return Block.BLOCKS[getBlockId(x, y, z)].getDefaultState();
    }

    @Override
    @Unique
    public BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        BlockState oldState = getBlockState(x, y, z);
        return setBlockId(x, y, z, blockState.getBlock().id) ? oldState : null;
    }

    @Override
    @Unique
    public BlockState setBlockStateWithMetadata(int x, int y, int z, BlockState blockState, int meta) {
        BlockState oldState = getBlockState(x, y, z);
        return setBlock(x, y, z, blockState.getBlock().id, meta) ? oldState : null;
    }
}
