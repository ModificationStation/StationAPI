package net.modificationstation.stationapi.api.client.texture.plugin;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BlockRendererPlugin {

    protected final BlockRenderer blockRenderer;

    public BlockRendererPlugin(BlockRenderer blockRenderer) {
        this.blockRenderer = blockRenderer;
    }

    public void renderWorld(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {}

    public void renderBed(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {}

    public void renderPlant(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {}

    public void renderCrops(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {}

    public void renderTorchTilted(BlockBase block, double renderX, double renderY, double renderZ, double width, double length, CallbackInfo ci) {}

    public void renderCrossed(BlockBase block, int meta, double x, double y, double z, CallbackInfo ci) {}

    public void renderShiftedColumn(BlockBase block, int meta, double x, double y, double z, CallbackInfo ci) {}

    public void renderFluid(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {}

    public void renderFast(BlockBase block, int x, int y, int z, float r, float g, float b, CallbackInfoReturnable<Boolean> cir) {}

    public void renderBottomFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {}

    public void renderTopFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {}

    public void renderEastFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {}

    public void renderWestFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {}

    public void renderNorthFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {}

    public void renderSouthFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {}

    public void renderInventory(BlockBase block, int meta, float brightness, CallbackInfo ci) {}
}
