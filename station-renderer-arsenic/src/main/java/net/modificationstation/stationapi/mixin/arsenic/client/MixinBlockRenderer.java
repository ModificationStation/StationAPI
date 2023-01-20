package net.modificationstation.stationapi.mixin.arsenic.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Rail;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BlockRenderer.class)
public abstract class MixinBlockRenderer {

    @Unique
    private final ArsenicBlockRenderer arsenic_plugin = new ArsenicBlockRenderer((BlockRenderer) (Object) this);

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public boolean renderBed(BlockBase block, int x, int y, int z) {
        return arsenic_plugin.renderBed(block, x, y, z);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public boolean renderCrossed(BlockBase block, int x, int y, int z) {
        return arsenic_plugin.renderPlant(block, x, y, z);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public boolean renderCrops(BlockBase block, int x, int y, int z) {
        return arsenic_plugin.renderCrops(block, x, y, z);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void renderTorchTilted(BlockBase block, double renderX, double renderY, double renderZ, double width, double length) {
        arsenic_plugin.renderTorchTilted(block, renderX, renderY, renderZ, width, length);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void method_47(BlockBase block, int meta, double x, double y, double z) {
        arsenic_plugin.renderCrossed(block, meta, x, y, z);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void method_56(BlockBase block, int meta, double x, double y, double z) {
        arsenic_plugin.renderShiftedColumn(block, meta, x, y, z);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public boolean renderLever(BlockBase i, int j, int k, int par4) {
        return arsenic_plugin.renderLever(i, j, k, par4);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public boolean renderFire(BlockBase i, int j, int k, int par4) {
        return arsenic_plugin.renderFire(i, j, k, par4);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public boolean renderRails(Rail i, int j, int k, int par4) {
        return arsenic_plugin.renderRails(i, j, k, par4);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public boolean renderLadder(BlockBase i, int j, int k, int par4) {
        return arsenic_plugin.renderLadder(i, j, k, par4);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public boolean renderFluid(BlockBase i, int j, int k, int par4) {
        return arsenic_plugin.renderFluid(i, j, k, par4);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void renderBottomFace(BlockBase block, double x, double y, double z, int texture) {
        arsenic_plugin.renderBottomFace(block, x, y, z, texture);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void renderTopFace(BlockBase block, double x, double y, double z, int texture) {
        arsenic_plugin.renderTopFace(block, x, y, z, texture);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void renderEastFace(BlockBase block, double x, double y, double z, int texture) {
        arsenic_plugin.renderEastFace(block, x, y, z, texture);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void renderWestFace(BlockBase block, double x, double y, double z, int texture) {
        arsenic_plugin.renderWestFace(block, x, y, z, texture);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void renderNorthFace(BlockBase block, double x, double y, double z, int texture) {
        arsenic_plugin.renderNorthFace(block, x, y, z, texture);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void renderSouthFace(BlockBase block, double x, double y, double z, int texture) {
        arsenic_plugin.renderSouthFace(block, x, y, z, texture);
    }

    @Inject(
            method = "render(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderInWorld(BlockBase block, int blockX, int blockY, int blockZ, CallbackInfoReturnable<Boolean> cir) {
        arsenic_plugin.renderWorld(block, blockX, blockY, blockZ, cir);
    }

    @Inject(
            method = "method_48(Lnet/minecraft/block/BlockBase;IF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getRenderType()I"
            ),
            cancellable = true
    )
    private void onRenderInInventory(BlockBase arg, int meta, float brightness, CallbackInfo ci) {
        arsenic_plugin.renderInventory(arg, meta, brightness, ci);
    }
}
