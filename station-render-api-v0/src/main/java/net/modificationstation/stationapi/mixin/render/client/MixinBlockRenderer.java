package net.modificationstation.stationapi.mixin.render.client;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.impl.client.model.BakedModelRendererImpl;
import net.modificationstation.stationapi.impl.client.texture.BlockRendererCustomAccessor;
import net.modificationstation.stationapi.impl.client.texture.StationBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BlockRenderer.class)
public class MixinBlockRenderer implements BlockRendererCustomAccessor {

    @Shadow private BlockView blockView;
    @Unique @Getter
    private final StationBlockRenderer stationBlockRenderer = new StationBlockRenderer((BlockRenderer) (Object) this);
    @Unique @Getter
    private final BakedModelRenderer bakedModelRenderer = new BakedModelRendererImpl((BlockRenderer) (Object) this);

    @Unique
    private boolean renderingInInventory;

    @Inject(
            method = "renderBed(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderBed_redirect(BlockBase block, int blockX, int blockY, int blockZ, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(stationBlockRenderer.renderBed(block, blockX, blockY, blockZ, renderingInInventory));
    }

    @Inject(
            method = "renderCrossed(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderPlant_redirect(BlockBase arg, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(stationBlockRenderer.renderPlant(arg, i, j, k, renderingInInventory));
    }

    @Inject(
            method = "renderCrops(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderCrops_redirect(BlockBase arg, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(stationBlockRenderer.renderCrops(arg, i, j, k, renderingInInventory));
    }

    @Inject(
            method = "method_47(Lnet/minecraft/block/BlockBase;IDDD)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderCrossed_redirect(BlockBase arg, int i, double d, double d1, double d2, CallbackInfo ci) {
        stationBlockRenderer.renderCrossed(arg, i, d, d1, d2, renderingInInventory);
        ci.cancel();
    }

    @Inject(
            method = "method_56(Lnet/minecraft/block/BlockBase;IDDD)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderShhiftedColumn_redirect(BlockBase arg, int i, double d, double d1, double d2, CallbackInfo ci) {
        stationBlockRenderer.renderShiftedColumn(arg, i, d, d1, d2, renderingInInventory);
        ci.cancel();
    }

    @Inject(
            method = "renderBottomFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderBottomFace_redirect(BlockBase arg, double d, double d1, double d2, int i, CallbackInfo ci) {
        stationBlockRenderer.renderBottomFace(arg, d, d1, d2, i, renderingInInventory);
        ci.cancel();
    }

    @Inject(
            method = "renderTopFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderTopFace_redirect(BlockBase arg, double d, double d1, double d2, int i, CallbackInfo ci) {
        stationBlockRenderer.renderTopFace(arg, d, d1, d2, i, renderingInInventory);
        ci.cancel();
    }

    @Inject(
            method = "renderEastFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderEastFace_redirect(BlockBase arg, double d, double d1, double d2, int i, CallbackInfo ci) {
        stationBlockRenderer.renderEastFace(arg, d, d1, d2, i, renderingInInventory);
        ci.cancel();
    }

    @Inject(
            method = "renderWestFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderWestFace_redirect(BlockBase arg, double d, double d1, double d2, int i, CallbackInfo ci) {
        stationBlockRenderer.renderWestFace(arg, d, d1, d2, i, renderingInInventory);
        ci.cancel();
    }

    @Inject(
            method = "renderNorthFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderNorthFace_redirect(BlockBase arg, double d, double d1, double d2, int i, CallbackInfo ci) {
        stationBlockRenderer.renderNorthFace(arg, d, d1, d2, i, renderingInInventory);
        ci.cancel();
    }

    @Inject(
            method = "renderSouthFace(Lnet/minecraft/block/BlockBase;DDDI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderSouthFace_redirect(BlockBase arg, double d, double d1, double d2, int i, CallbackInfo ci) {
        stationBlockRenderer.renderSouthFace(arg, d, d1, d2, i, renderingInInventory);
        ci.cancel();
    }

    @Inject(
            method = "method_48(Lnet/minecraft/block/BlockBase;IF)V",
            at = @At("HEAD")
    )
    private void setRenderingInInventory1(BlockBase arg, int i, float f, CallbackInfo ci) {
        renderingInInventory = true;
    }

    @Inject(
            method = "method_48(Lnet/minecraft/block/BlockBase;IF)V",
            at = @At("RETURN")
    )
    private void setRenderingInInventory2(BlockBase arg, int i, float f, CallbackInfo ci) {
        renderingInInventory = false;
    }

    @Inject(
            method = "method_53(Lnet/minecraft/block/BlockBase;Lnet/minecraft/level/Level;III)V",
            at = @At("RETURN")
    )
    private void renderFallingBlockAtlases(BlockBase arg, Level arg1, int i, int j, int k, CallbackInfo ci) {
        stationBlockRenderer.renderActiveAtlases();
    }

    @Inject(
            method = "render(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;updateBoundingBox(Lnet/minecraft/level/BlockView;III)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void onRenderInWorld(BlockBase block, int blockX, int blockY, int blockZ, CallbackInfoReturnable<Boolean> cir) {
        if (block instanceof BlockWithWorldRenderer)
            cir.setReturnValue(((BlockWithWorldRenderer) block).renderWorld((BlockRenderer) (Object) this, blockView, blockX, blockY, blockZ));
    }

    @Inject(
            method = "method_48(Lnet/minecraft/block/BlockBase;IF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getRenderType()I"
            ),
            cancellable = true
    )
    private void onRenderInInventory(BlockBase arg, int i, float f, CallbackInfo ci) {
        if (arg instanceof BlockWithInventoryRenderer) {
            ((BlockWithInventoryRenderer) arg).renderInventory((BlockRenderer) (Object) this, i);
            renderingInInventory = false;
            ci.cancel();
        }
    }
}
