package net.modificationstation.stationapi.mixin.resourceloader.client;

import net.minecraft.client.resource.pack.TexturePack;
import net.minecraft.client.resource.pack.ZippedTexturePack;
import net.modificationstation.stationapi.api.client.resource.ReloadableAssetsManager;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;

@Mixin(ZippedTexturePack.class)
class ZipTexturePackMixin extends TexturePack {
    @Inject(
            method = "method_976",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_getFromResourceManager(String path, CallbackInfoReturnable<InputStream> cir) {
        ReloadableAssetsManager.INSTANCE.getResource(Identifier.of(path)).map(resource -> {
            try {
                return resource.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).ifPresent(cir::setReturnValue);
    }
}
