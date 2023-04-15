package net.modificationstation.stationapi.mixin.resourceloader.client;

import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.resource.ZippedTexturePack;
import net.modificationstation.stationapi.api.client.resource.ReloadableAssetsManager;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.co.benjiweber.expressions.exception.Exceptions;

import java.io.InputStream;

@Mixin(ZippedTexturePack.class)
public class MixinZippedTexturePack extends TexturePack {

    @Inject(
            method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getFromResourceManager(String path, CallbackInfoReturnable<InputStream> cir) {
        ReloadableAssetsManager.INSTANCE.getResource(Identifier.of(path)).map(Exceptions.unchecked(Resource::getInputStream)).ifPresent(cir::setReturnValue);
    }
}
