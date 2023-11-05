package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonExtensionBlock;
import net.modificationstation.stationapi.impl.block.StationFlatteningMovingPistonImpl;
import net.modificationstation.stationapi.impl.block.StationFlatteningPistonBlockEntityImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonExtensionBlock.class)
class PistonExtensionBlockMixin {
    @Inject(
            method = "method_1533",
            at = @At("RETURN")
    )
    private static void stationapi_setPushedBlockState(int blockId, int blockMeta, int k, boolean bl, boolean bl2, CallbackInfoReturnable<BlockEntity> cir) {
        ((StationFlatteningPistonBlockEntityImpl) cir.getReturnValue()).stationapi_setPushedBlockState(StationFlatteningMovingPistonImpl.pushedBlockState);
        StationFlatteningMovingPistonImpl.pushedBlockState = null;
    }
}
