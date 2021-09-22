package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.resource.ZippedTexturePack;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.impl.client.resource.ResourceImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.*;
import java.util.zip.*;

@Mixin(ZippedTexturePack.class)
public class MixinZippedTexturePack {

    @Shadow private ZipFile zipFile;

    @Inject(method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;", at = @At("HEAD"), cancellable = true)
    private void getExpandableAtlas(String name, CallbackInfoReturnable<InputStream> cir) {
        ExpandableAtlas atlas = ExpandableAtlas.getByPath(name);
        if (atlas != null)
            cir.setReturnValue(atlas.getStream());
    }

    @Inject(method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;", at = @At(value = "RETURN", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void retrieveMeta(String name, CallbackInfoReturnable<InputStream> cir, ZipEntry entry) {
        InputStream resource = cir.getReturnValue();
        if (resource != null) {
            InputStream meta = null;
            try {
                ZipEntry metaEntry = zipFile.getEntry(name.substring(1) + ".mcmeta");
                meta = metaEntry == null ? null : zipFile.getInputStream(metaEntry);
            } catch (IOException ignored) {}
            cir.setReturnValue(new ResourceImpl(resource, meta));
        }
    }

    @Inject(method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void retrieveMeta(String name, CallbackInfoReturnable<InputStream> cir) {
        InputStream resource = cir.getReturnValue();
        if (resource != null)
            cir.setReturnValue(new ResourceImpl(resource, TexturePack.class.getResourceAsStream(name + ".mcmeta")));
    }
}
