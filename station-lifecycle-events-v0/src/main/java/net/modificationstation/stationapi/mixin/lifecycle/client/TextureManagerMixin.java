package net.modificationstation.stationapi.mixin.lifecycle.client;

import net.minecraft.class_303;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
class TextureManagerMixin {
    @Shadow private class_303 field_1256;

    @Inject(
            method = "method_1096",
            at = @At("HEAD")
    )
    private void stationapi_beforeTextureRefresh(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                TexturePackLoadedEvent.Before.builder()
                        .textureManager((TextureManager) (Object) this)
                        .newTexturePack(field_1256.field_1175)
                        .build()
        );
    }

    @Inject(
            method = "method_1096",
            at = @At("RETURN")
    )
    private void stationapi_texturesRefresh(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                TexturePackLoadedEvent.After.builder()
                        .textureManager((TextureManager) (Object) this)
                        .newTexturePack(field_1256.field_1175)
                        .build()
        );
    }
}
