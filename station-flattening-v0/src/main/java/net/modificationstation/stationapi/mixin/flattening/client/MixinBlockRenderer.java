package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.world.BlockStateView;
import net.modificationstation.stationapi.impl.block.BlockBrightness;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockRenderManager.class, priority = 500)
public class MixinBlockRenderer {
    @Shadow private BlockView blockView;

    @Inject(method = "render", at = @At("HEAD"))
    private void captureLightEmission(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
        if (blockView instanceof BlockStateView stateView) {
            BlockState state = stateView.getBlockState(x, y, z);
            BlockBrightness.light = state.getLuminance();
        }
        else BlockBrightness.light = Block.BLOCKS_LIGHT_LUMINANCE[block.id];
    }
}
