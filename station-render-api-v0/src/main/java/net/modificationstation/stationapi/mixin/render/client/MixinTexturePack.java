package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.*;

@Mixin(TexturePack.class)
public class MixinTexturePack {

    @Inject(method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;", at = @At("HEAD"), cancellable = true)
    private void getExpandableAtlas(String name, CallbackInfoReturnable<InputStream> cir) {
        ExpandableAtlas atlas = ExpandableAtlas.getByPath(name);
        if (atlas != null)
            cir.setReturnValue(atlas.getStream());
    }
}
