package net.modificationstation.stationapi.mixin.resourceloader.client;

import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.resource.FakeResources;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.impl.resource.ModResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.co.benjiweber.expressions.exception.Exceptions;

import java.io.InputStream;
import java.nio.file.Files;

@Mixin(TexturePack.class)
public class MixinTexturePack {

    /**
     * Highest priority
     */
    @Inject(
            method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void modResources(String path, CallbackInfoReturnable<InputStream> cir) {
        ModResources.getTopPath(path).map(Exceptions.unchecked(Files::newInputStream)).ifPresent(cir::setReturnValue);
    }

    /**
     * Lowest priority
     */
    @Inject(
            method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void fakeResources(String path, CallbackInfoReturnable<InputStream> cir) {
        FakeResources.get(path).map(Exceptions.unchecked(Resource::getInputStream)).ifPresent(cir::setReturnValue);
    }
}
