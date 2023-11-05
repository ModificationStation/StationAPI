package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.world.BlockStateView;
import net.modificationstation.stationapi.impl.block.BlockBrightness;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockRenderManager.class, priority = 500)
class BlockRenderManagerMixin {
    @Shadow private BlockView blockView;

    @Inject(
            method = "render(Lnet/minecraft/block/Block;III)Z",
            at = @At("HEAD")
    )
    private void stationapi_captureLightEmission(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
        BlockBrightness.light = blockView instanceof BlockStateView stateView ? stateView.getBlockState(x, y, z).getLuminance() : Block.BLOCKS_LIGHT_LUMINANCE[block.id];
    }
}
