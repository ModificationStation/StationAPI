package net.modificationstation.stationapi.mixin.resourceloader.client;

import net.minecraft.class_285;
import net.modificationstation.stationapi.api.client.resource.ReloadableAssetsManager;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.co.benjiweber.expressions.exception.Exceptions;

import java.io.InputStream;

@Mixin(class_285.class)
class TexturePackMixin {
    @Inject(
            method = "method_976",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_getFromResourceManager(String path, CallbackInfoReturnable<InputStream> cir) {
        ReloadableAssetsManager.INSTANCE.getResource(Identifier.of(path)).map(Exceptions.unchecked(Resource::getInputStream)).ifPresent(cir::setReturnValue);
    }
}
