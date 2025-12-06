package net.modificationstation.stationapi.mixin.arsenic.client.particle;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.particle.ItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.Tessellator;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.ATLAS_SIZE;

@Mixin(ItemParticle.class)
class ItemParticleMixin extends Particle {
    private ItemParticleMixin(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
    }

    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    private void stationapi_initializeSprite(
            Tessellator f, float g, float h, float i, float j, float k, float par7, CallbackInfo ci,
            @Share("sprite") LocalRef<Sprite> spriteRef
    ) {
        spriteRef.set(Atlases.getGuiItems().getTexture(textureId).getSprite());
    }

    @ModifyVariable(
            method = "render",
            at = @At("STORE"),
            index = 8
    )
    private float stationapi_modStartU(
            float value,
            @Share("sprite") LocalRef<Sprite> spriteRef
    ) {
        Sprite sprite = spriteRef.get();
        return sprite.getMinU() + (sprite.getMaxU() - sprite.getMinU()) * prevU / 4;
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(
                    floatValue = 3.996F / ATLAS_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_modEndU(
            float constant,
            @Share("sprite") LocalRef<Sprite> spriteRef
    ) {
        Sprite sprite = spriteRef.get();
        return (sprite.getMaxU() - sprite.getMinU()) / 3.996F;
    }

    @ModifyVariable(
            method = "render",
            at = @At("STORE"),
            index = 10
    )
    private float stationapi_modStartV(
            float value,
            @Share("sprite") LocalRef<Sprite> spriteRef
    ) {
        Sprite sprite = spriteRef.get();
        return sprite.getMinV() + (sprite.getMaxV() - sprite.getMinV()) * prevV / 4;
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(
                    floatValue = 3.996F / ATLAS_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_modEndV(
            float constant,
            @Share("sprite") LocalRef<Sprite> spriteRef
    ) {
        Sprite sprite = spriteRef.get();
        return (sprite.getMaxV() - sprite.getMinV()) / 3.996F;
    }
}
