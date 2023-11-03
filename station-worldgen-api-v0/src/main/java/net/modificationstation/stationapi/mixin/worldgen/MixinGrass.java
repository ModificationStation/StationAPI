package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.block.GrassBlock;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrassBlock.class)
public class MixinGrass {
    @Inject(
            method = "getColourMultiplier",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getBiomeColor(BlockView view, int x, int y, int z, CallbackInfoReturnable<Integer> info) {
        int color = BiomeColorsImpl.GRASS_INTERPOLATOR.getColor(view.method_1781(), x, z);
        info.setReturnValue(color);
    }
}
