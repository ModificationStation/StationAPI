package net.modificationstation.stationapi.mixin.arsenic.client.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.client.render.block.BlockRenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.client.render.block.entity.PistonBlockEntityRenderer.class)
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
            PistonBlockEntity pistonBlockEntity, double renderX, double renderY, double renderZ, float partialTicks
    ) {
        manager.renderAllSides(pistonBlockEntity.getPushedBlockState(), x, y, z);
    }
}
