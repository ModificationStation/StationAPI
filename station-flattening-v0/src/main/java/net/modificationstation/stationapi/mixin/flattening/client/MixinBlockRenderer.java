package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.world.BlockStateView;
import net.modificationstation.stationapi.impl.block.BlockBrightness;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockRenderer.class, priority = 500)
public class MixinBlockRenderer {
    @Shadow private BlockView blockView;

    @Inject(method = "render", at = @At("HEAD"))
    private void captureLightEmission(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
        if (blockView instanceof BlockStateView stateView) {
            BlockState state = stateView.getBlockState(x, y, z);
            BlockBrightness.light = state.getLuminance();
        }
        else BlockBrightness.light = BlockBase.EMITTANCE[block.id];
    }
}
