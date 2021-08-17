package net.modificationstation.stationapi.mixin.render.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.ParticleBase;
import net.modificationstation.stationapi.impl.client.texture.StationParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public class MixinParticleManager {

    private final StationParticleManager stationParticleManager = new StationParticleManager();

    @Inject(
            method = "method_324(Lnet/minecraft/entity/EntityBase;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ParticleBase;method_2002(Lnet/minecraft/client/render/Tessellator;FFFFFF)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void checkParticle(EntityBase arg, float f, CallbackInfo ci, float var3, float var4, float var5, float var6, float var7, int var8, int var9, Tessellator var10, int var11, ParticleBase var12) {
        stationParticleManager.checkParticle(var12);
    }

    @Inject(
            method = "method_324(Lnet/minecraft/entity/EntityBase;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;draw()V",
                    shift = At.Shift.AFTER
            )
    )
    private void renderAtlases(EntityBase arg, float f, CallbackInfo ci) {
        stationParticleManager.renderAtlases();
    }
}
