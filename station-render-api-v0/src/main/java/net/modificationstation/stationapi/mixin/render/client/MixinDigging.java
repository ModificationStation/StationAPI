package net.modificationstation.stationapi.mixin.render.client;

import lombok.Getter;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.particle.Digging;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.impl.client.texture.StationDiggingParticle;
import net.modificationstation.stationapi.impl.client.texture.StationDiggingParticleProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Digging.class)
public class MixinDigging implements StationDiggingParticleProvider {

    @Getter
    private StationDiggingParticle stationDiggingParticle;

    @Inject(
            method = "<init>(Lnet/minecraft/level/Level;DDDDDDLnet/minecraft/block/BlockBase;II)V",
            at = @At("RETURN")
    )
    private void onCor(Level arg, double d, double d1, double d2, double d3, double d4, double d5, BlockBase arg1, int i, int j, CallbackInfo ci) {
        stationDiggingParticle = new StationDiggingParticle((Digging) (Object) this);
    }

    @Inject(
            method = "method_2002(Lnet/minecraft/client/render/Tessellator;FFFFFF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void redirectRender(Tessellator arg, float f, float f1, float f2, float f3, float f4, float f5, CallbackInfo ci) {
        stationDiggingParticle.render(f, f1, f2, f3, f4, f5);
        ci.cancel();
    }
}
