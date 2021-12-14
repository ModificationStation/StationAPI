package net.modificationstation.stationapi.mixin.render.client;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.impl.client.model.BakedModelRendererImpl;
import net.modificationstation.stationapi.impl.client.texture.BlockRendererCustomAccessor;
import net.modificationstation.stationapi.impl.client.texture.StationBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public boolean renderBed(BlockBase block, int x, int y, int z) {
        return stationBlockRenderer.renderBed(block, x, y, z);
    }

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public boolean renderCrossed(BlockBase block, int x, int y, int z) {
        return stationBlockRenderer.renderPlant(block, x, y, z);
    }

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public boolean renderCrops(BlockBase block, int x, int y, int z) {
        return stationBlockRenderer.renderCrops(block, x, y, z);
    }

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public void method_47(BlockBase block, int meta, double x, double y, double z) {
        stationBlockRenderer.renderCrossed(block, meta, x, y, z);
    }

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public void method_56(BlockBase arg, int meta, double x, double y, double z) {
        stationBlockRenderer.renderShiftedColumn(arg, meta, x, y, z);
    }

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public boolean renderFast(BlockBase block, int x, int y, int z, float r, float g, float b) {
        return stationBlockRenderer.renderFast(block, x, y, z, r, g, b);
    }

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public void renderBottomFace(BlockBase block, double x, double y, double z, int texture) {
        stationBlockRenderer.renderBottomFace(block, x, y, z, texture);
    }

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public void renderTopFace(BlockBase block, double x, double y, double z, int texture) {
        stationBlockRenderer.renderTopFace(block, x, y, z, texture);
    }

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public void renderEastFace(BlockBase block, double x, double y, double z, int texture) {
        stationBlockRenderer.renderEastFace(block, x, y, z, texture);
    }

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public void renderWestFace(BlockBase block, double x, double y, double z, int texture) {
        stationBlockRenderer.renderWestFace(block, x, y, z, texture);
    }

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public void renderNorthFace(BlockBase block, double x, double y, double z, int texture) {
        stationBlockRenderer.renderNorthFace(block, x, y, z, texture);
    }

    /**
     * This is done to allow to completely override renderer with a plugin.
     * It's either I inject it at head and always cancel, or just overwrite, the latter being more GC friendly.
     * @author mine_diver
     */
    @Overwrite
    public void renderSouthFace(BlockBase block, double x, double y, double z, int texture) {
        stationBlockRenderer.renderSouthFace(block, x, y, z, texture);
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
    private void onRenderInInventory(BlockBase arg, int meta, float brightness, CallbackInfo ci) {
        if (arg instanceof BlockWithInventoryRenderer) {
            ((BlockWithInventoryRenderer) arg).renderInventory((BlockRenderer) (Object) this, meta);
            ci.cancel();
        }
    }
}
