package net.modificationstation.stationapi.mixin.arsenic.client.block.entity;

import net.minecraft.block.Block;
import net.minecraft.class_282;
import net.minecraft.class_283;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_282.class)
class PistonBlockEntityRenderer {
    @Redirect(
            method = "render(Lnet/minecraft/class_283;DDDF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/BlockRenderManager;renderWithoutCulling(Lnet/minecraft/block/Block;III)V"
            )
    )
    private void stationapi_renderBlockState(
            BlockRenderManager manager, Block block, int x, int y, int z,
            class_283 pistonBlockEntity, double renderX, double renderY, double renderZ, float partialTicks
    ) {
        manager.renderAllSides(Tessellator.INSTANCE, pistonBlockEntity.getPushedBlockState(), x, y, z);
    }
}
