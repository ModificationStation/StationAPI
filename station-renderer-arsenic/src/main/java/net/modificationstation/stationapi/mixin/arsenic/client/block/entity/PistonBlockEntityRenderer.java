package net.modificationstation.stationapi.mixin.arsenic.client.block.entity;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.tileentity.PistonRenderer;
import net.minecraft.tileentity.TileEntityPiston;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PistonRenderer.class)
public class PistonBlockEntityRenderer {
    @Redirect(
            method = "method_973",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/BlockRenderer;renderAllSides(Lnet/minecraft/block/BlockBase;III)V"
            )
    )
    private void stationapi_renderBlockState(
            BlockRenderer manager, BlockBase block, int x, int y, int z,
            TileEntityPiston pistonBlockEntity, double renderX, double renderY, double renderZ, float partialTicks
    ) {
        manager.renderAllSides(pistonBlockEntity.getPushedBlockState(), x, y, z);
    }
}
