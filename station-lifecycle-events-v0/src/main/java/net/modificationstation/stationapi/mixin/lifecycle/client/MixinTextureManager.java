package net.modificationstation.stationapi.mixin.lifecycle.client;

import net.minecraft.client.TexturePackManager;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
public class MixinTextureManager {

    @Shadow private TexturePackManager texturePackManager;

    @Inject(
            method = "reloadTexturesFromTexturePack()V",
            at = @At("HEAD")
    )
    private void beforeTextureRefresh(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                TexturePackLoadedEvent.Before.builder()
                        .textureManager((TextureManager) (Object) this)
                        .newTexturePack(texturePackManager.texturePack)
                        .build()
        );
    }

    @Inject(
            method = "reloadTexturesFromTexturePack()V",
            at = @At("RETURN")
    )
    private void texturesRefresh(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                TexturePackLoadedEvent.After.builder()
                        .textureManager((TextureManager) (Object) this)
                        .newTexturePack(texturePackManager.texturePack)
                        .build()
        );
    }
}
