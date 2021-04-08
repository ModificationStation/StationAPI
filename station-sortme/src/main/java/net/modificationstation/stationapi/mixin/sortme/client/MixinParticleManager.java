package net.modificationstation.stationapi.mixin.sortme.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.ParticleBase;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;
import net.modificationstation.stationapi.mixin.sortme.client.accessor.ParticleBaseAccessor;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public class MixinParticleManager {

    @Shadow
    private TextureManager textureManager;

    /*@Inject(method = "renderParticles(Lnet/minecraft/src/Entity;F)V", at = @At("HEAD"))
    private void onRenderParticles(CallbackInfo ci) {
        currentTexture = -1;
    }

    @Inject(method = "renderParticles(Lnet/minecraft/src/Entity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;getTexture(Ljava/lang/String;)I", ordinal = 1, shift = At.Shift.AFTER))
    private void onTerrainTexture(CallbackInfo ci) {
        currentTexture = 0;
    }*/
    private TextureRegistry registryToBind;
    private int textureToBind;

    @Redirect(method = "method_324(Lnet/minecraft/entity/EntityBase;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;getTextureId(Ljava/lang/String;)I"))
    private int onGetTexture(TextureManager textureManager, String texture) {
        registryToBind = null;
        if (TextureRegistry.currentRegistry() == null || !TextureRegistry.currentRegistry().getAtlas(TextureRegistry.currentRegistry().currentTexture()).equals(texture))
            for (TextureRegistry registry : TextureRegistry.registries()) {
                Integer atlasID = registry.getAtlasID(texture);
                if (atlasID != null) {
                    registryToBind = registry;
                    textureToBind = atlasID;
                    break;
                }
            }
        return textureManager.getTextureId(texture);
    }

    @Redirect(method = "method_324(Lnet/minecraft/entity/EntityBase;F)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", remap = false))
    private void onBindTexture(int texture, int textureID) {
        if (registryToBind == null)
            GL11.glBindTexture(texture, textureID);
        else
            registryToBind.bindAtlas(textureManager, textureToBind);
    }

    @Redirect(method = "method_324(Lnet/minecraft/entity/EntityBase;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ParticleBase;method_2002(Lnet/minecraft/client/render/Tessellator;FFFFFF)V"))
    private void onRenderParticles(ParticleBase particleBase, Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
        if (TextureRegistry.currentRegistry() != null) {
            int atlasID = ((ParticleBaseAccessor) particleBase).getField_2635() / TextureRegistry.currentRegistry().texturesPerFile();
            if (atlasID != TextureRegistry.currentRegistry().currentTexture()) {
                Tessellator tessellator = Tessellator.INSTANCE;
                tessellator.draw();
                TextureRegistry.currentRegistry().bindAtlas(textureManager, atlasID);
                tessellator.start();
            }
        }
        /*if (currentTexture != -1 && entityFX instanceof EntityDiggingFX) {
            EntityDiggingFX entityDiggingFX = (EntityDiggingFX) entityFX;
            int atlasID = ((EntityFXAccessor) entityDiggingFX).getParticleTextureIndex() / TERRAIN.texturesPerFile();
            if (TERRAIN.currentTexture() != atlasID) {
                Tessellator tessellator = Tessellator.instance;
                tessellator.draw();
                TERRAIN.bindAtlas(renderer, atlasID);
                tessellator.startDrawingQuads();
            }
        }*/
        particleBase.method_2002(var1, var2, var3, var4, var5, var6, var7);
    }
    //private int currentTexture;
}
