package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.world.World;
import net.minecraft.world.WorldRegion;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.world.StationFlatteningWorldPopulationRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WorldRegion.class)
class ChunkCacheMixin implements StationFlatteningWorldPopulationRegion {
    @Shadow private int minX;
    @Shadow private int minZ;
    @Shadow private Chunk[][] chunks;
    @Shadow private World world;

    @Override
    @Unique
    public BlockState getBlockState(int x, int y, int z) {
        if (y >= world.getBottomY() && y < world.getTopY()) {
            int var4 = (x >> 4) - this.minX;
            int var5 = (z >> 4) - this.minZ;
            if (var4 >= 0 && var4 < this.chunks.length && var5 >= 0 && var5 < this.chunks[var4].length) {
                Chunk var6 = this.chunks[var4][var5];
                return var6 == null ? States.AIR.get() : var6.getBlockState(x & 15, y, z & 15);
            }
        }
        return States.AIR.get();
    }
    
    @ModifyConstant(method = {
            "method_142",
            "getBlockId",
            "getBlockMeta"
    }, constant = @Constant(intValue = 128))
    private int stationapi_changeMaxHeight(int value) {
        return world.getTopY();
    }

    @ModifyConstant(method = {
            "method_142",
            "getBlockId",
            "getBlockMeta"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO, ordinal = 0))
    private int stationapi_changeMinHeightGE(int value) {
        return world.getBottomY();
    }
}
