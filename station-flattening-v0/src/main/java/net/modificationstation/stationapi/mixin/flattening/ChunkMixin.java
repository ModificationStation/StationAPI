package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.Block;
import net.minecraft.class_43;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.world.chunk.StationFlatteningChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(class_43.class)
abstract class ChunkMixin implements StationFlatteningChunk {
    @Shadow public abstract int method_859(int i, int j, int k);

    @Shadow public abstract boolean method_860(int i, int j, int k, int l);

    @Override
    @Unique
    public BlockState getBlockState(int x, int y, int z) {
        return Block.BLOCKS[method_859(x, y, z)].getDefaultState();
    }

    @Override
    @Unique
    public BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        BlockState oldState = getBlockState(x, y, z);
        return method_860(x, y, z, blockState.getBlock().id) ? oldState : null;
    }
}
