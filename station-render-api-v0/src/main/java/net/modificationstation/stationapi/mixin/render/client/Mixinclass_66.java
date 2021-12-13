package net.modificationstation.stationapi.mixin.render.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_66;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.WorldPopulationRegion;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.impl.client.texture.StationChunkRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Environment(EnvType.CLIENT)
@Mixin(class_66.class)
public class Mixinclass_66 {

    private final StationChunkRenderer stationChunkRenderer = new StationChunkRenderer();

    @Inject(
            method = "method_296()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void startMeshRender(CallbackInfo ci, int var1, int var2, int var3, int var4, int var5, int var6, HashSet<List<TileEntityBase>> var21, int var8, WorldPopulationRegion var9, BlockRenderer var10) {
        stationChunkRenderer.startMeshRender(var10);
    }

    @Inject(
            method = "method_296()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;draw()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void startMeshDraw(CallbackInfo ci, int var1, int var2, int var3, int var4, int var5, int var6, HashSet<List<TileEntityBase>> var21, int var8, WorldPopulationRegion var9, BlockRenderer var10) {
        stationChunkRenderer.endMeshRender(var10);
    }
}
