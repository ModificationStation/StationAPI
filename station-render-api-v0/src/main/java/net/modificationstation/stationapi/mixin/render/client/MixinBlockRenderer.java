package net.modificationstation.stationapi.mixin.render.client;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Rail;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.texture.plugin.BlockRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.BlockRendererPluginProvider;
import net.modificationstation.stationapi.api.client.texture.plugin.RenderPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BlockRenderer.class)
public class MixinBlockRenderer implements BlockRendererPluginProvider {

    @Shadow private BlockView blockView;
    @Unique @Getter
    private final BlockRendererPlugin plugin = RenderPlugin.PLUGIN.createBlockRenderer((BlockRenderer) (Object) this);

    @Inject(
            method = "renderBed(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderBed(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        plugin.renderBed(block, x, y, z, cir);
    }

    @Inject(
            method = "renderCrossed(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderCrossed(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        plugin.renderPlant(block, x, y, z, cir);
    }

    @Inject(
            method = "renderCrops(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderCrops(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        plugin.renderCrops(block, x, y, z, cir);
    }

    @Inject(
            method = "renderTorchTilted(Lnet/minecraft/block/BlockBase;DDDDD)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderTorchTilted(BlockBase block, double renderX, double renderY, double renderZ, double width, double length, CallbackInfo ci) {
        plugin.renderTorchTilted(block, renderX, renderY, renderZ, width, length, ci);
    }

    @Inject(
            method = "method_47(Lnet/minecraft/block/BlockBase;IDDD)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void method_47(BlockBase block, int meta, double x, double y, double z, CallbackInfo ci) {
        plugin.renderCrossed(block, meta, x, y, z, ci);
    }

    @Inject(
            method = "method_56(Lnet/minecraft/block/BlockBase;IDDD)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void method_56(BlockBase block, int meta, double x, double y, double z, CallbackInfo ci) {
        plugin.renderShiftedColumn(block, meta, x, y, z, ci);
    }

    @Inject(
            method = "renderLever(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderLever(BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        plugin.renderLever(i, j, k, par4, cir);
    }

    @Inject(
            method = "renderFire(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderFire(BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        plugin.renderFire(i, j, k, par4, cir);
    }

    @Inject(
            method = "renderRails(Lnet/minecraft/block/Rail;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderRails(Rail i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        plugin.renderRails(i, j, k, par4, cir);
    }

    @Inject(
            method = "renderLadder(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderLadder(BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        plugin.renderLadder(i, j, k, par4, cir);
    }

    @Inject(
            method = "renderFluid(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderFluid(BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        plugin.renderFluid(i, j, k, par4, cir);
    }

    @Inject(
            method = "renderFast(Lnet/minecraft/block/BlockBase;IIIFFF)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderFast(BlockBase block, int x, int y, int z, float r, float g, float b, CallbackInfoReturnable<Boolean> cir) {
        plugin.renderFast(block, x, y, z, r, g, b, cir);
    }

    @Inject(
            method = "renderBottomFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderBottomFace(BlockBase block, double x, double y, double z, int texture, CallbackInfo ci) {
        plugin.renderBottomFace(block, x, y, z, texture, ci);
    }

    @Inject(
            method = "renderTopFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderTopFace(BlockBase block, double x, double y, double z, int texture, CallbackInfo ci) {
        plugin.renderTopFace(block, x, y, z, texture, ci);
    }

    @Inject(
            method = "renderEastFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderEastFace(BlockBase block, double x, double y, double z, int texture, CallbackInfo ci) {
        plugin.renderEastFace(block, x, y, z, texture, ci);
    }

    @Inject(
            method = "renderWestFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderWestFace(BlockBase block, double x, double y, double z, int texture, CallbackInfo ci) {
        plugin.renderWestFace(block, x, y, z, texture, ci);
    }

    @Inject(
            method = "renderNorthFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderNorthFace(BlockBase block, double x, double y, double z, int texture, CallbackInfo ci) {
        plugin.renderNorthFace(block, x, y, z, texture, ci);
    }

    @Inject(
            method = "renderSouthFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderSouthFace(BlockBase block, double x, double y, double z, int texture, CallbackInfo ci) {
        plugin.renderSouthFace(block, x, y, z, texture, ci);
    }

    @Inject(
            method = "render(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderInWorld(BlockBase block, int blockX, int blockY, int blockZ, CallbackInfoReturnable<Boolean> cir) {
        plugin.renderWorld(block, blockX, blockY, blockZ, cir);
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
        plugin.renderInventory(arg, meta, brightness, ci);
        if (arg instanceof BlockWithInventoryRenderer) {
            ((BlockWithInventoryRenderer) arg).renderInventory((BlockRenderer) (Object) this, meta);
            ci.cancel();
        }
    }
}
