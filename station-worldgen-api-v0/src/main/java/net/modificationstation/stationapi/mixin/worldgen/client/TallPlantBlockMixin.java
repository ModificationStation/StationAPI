package net.modificationstation.stationapi.mixin.worldgen.client;

import net.minecraft.block.TallPlantBlock;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TallPlantBlock.class)
class TallPlantBlockMixin {
    @Inject(
            method = "getColorMultiplier",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_getBiomeColor(BlockView view, int x, int y, int z, CallbackInfoReturnable<Integer> info) {
        if (view.getBlockMeta(x, y, z) != 0) {
            long l = x * 3129871L + z * 6129781L + y;
            l = l * l * 42317861L + l * 11L;
            x += (int) (l >> 14) & 7;
            z += (int) (l >> 24) & 7;
            
            int color = BiomeColorsImpl.GRASS_INTERPOLATOR.getColor(view.method_1781(), x, z);
            info.setReturnValue(color);
        }
    }
}
