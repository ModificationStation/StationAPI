package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Dimension.class)
public class DimensionMixin {
    @Shadow public World world;

    @Inject(method = "isValidSpawnPoint", at = @At("HEAD"), cancellable = true)
    private void fixSpawnPosition(int x, int z, CallbackInfoReturnable<Boolean> info) {
        int blockID = this.world.getSpawnBlockId(x, z);
        Block block = Block.BLOCKS[blockID];
        info.setReturnValue(block != null && block.isFullCube() && block.isOpaque());
    }
}
