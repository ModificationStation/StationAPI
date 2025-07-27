package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.block.Block;
import net.minecraft.client.particle.BlockParticle;
import net.minecraft.client.render.Tessellator;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.particle.ArsenicDiggingParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockParticle.class)
class BlockParticleMixin {
    @Unique
    private ArsenicDiggingParticle stationapi$arsenicDiggingParticle;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void stationapi_onCor(World arg, double d, double d1, double d2, double d3, double d4, double d5, Block arg1, int i, int j, CallbackInfo ci) {
        stationapi$arsenicDiggingParticle = new ArsenicDiggingParticle((BlockParticle) (Object) this);
    }

    @Inject(
            method = "color",
            at = @At("HEAD")
    )
    private void stationapi_checkBlockCoords(int i, int j, int k, CallbackInfoReturnable<BlockParticle> cir) {
        stationapi$arsenicDiggingParticle.checkBlockCoords(i, j, k);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void render(Tessellator arg, float f, float f1, float f2, float f3, float f4, float f5) {
        stationapi$arsenicDiggingParticle.render(arg, f, f1, f2, f3, f4, f5);
    }
}
