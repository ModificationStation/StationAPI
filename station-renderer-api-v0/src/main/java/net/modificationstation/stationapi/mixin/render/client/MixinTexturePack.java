package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.impl.client.resource.ResourceImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;

@Mixin(TexturePack.class)
public abstract class MixinTexturePack {

    @Inject(method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;", at = @At("RETURN"), cancellable = true)
    private void retrieveMeta(String name, CallbackInfoReturnable<InputStream> cir) {
        InputStream resource = cir.getReturnValue();
        if (resource != null)
            cir.setReturnValue(new ResourceImpl(resource, TexturePack.class.getResourceAsStream(name + ".mcmeta")));
    }
}
