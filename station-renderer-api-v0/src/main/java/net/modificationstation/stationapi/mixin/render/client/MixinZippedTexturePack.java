package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.resource.ZippedTexturePack;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.impl.client.resource.ResourceImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.GUI_ITEMS;
import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.TERRAIN;

@Mixin(ZippedTexturePack.class)
public class MixinZippedTexturePack extends TexturePack {

    @Shadow private ZipFile zipFile;

    @Inject(method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;", at = @At(value = "RETURN", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void retrieveMeta(String name, CallbackInfoReturnable<InputStream> cir, ZipEntry entry) {
        InputStream resource = cir.getReturnValue();
        if (resource == null) {
            Identifier identifier = ResourceManager.ASSETS.toId(name, MODID + "/textures", "png");
            if (TERRAIN.slicedSpritesheetView.containsKey(identifier)) {
                BufferedImage image = TERRAIN.slicedSpritesheetView.get(identifier);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "png", outputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                resource = new ByteArrayInputStream(outputStream.toByteArray());
            } else if (GUI_ITEMS.slicedSpritesheetView.containsKey(identifier)) {
                BufferedImage image = GUI_ITEMS.slicedSpritesheetView.get(identifier);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "png", outputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                resource = new ByteArrayInputStream(outputStream.toByteArray());
            }
        }
        if (resource != null) {
            InputStream meta = null;
            try {
                ZipEntry metaEntry = zipFile.getEntry(name.substring(1) + ".mcmeta");
                meta = metaEntry == null ? null : zipFile.getInputStream(metaEntry);
            } catch (IOException ignored) {}
            cir.setReturnValue(new ResourceImpl(resource, meta, this.name));
        }
    }

    @Inject(method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void retrieveMeta(String name, CallbackInfoReturnable<InputStream> cir) {
        InputStream resource = cir.getReturnValue();
        if (resource != null)
            cir.setReturnValue(new ResourceImpl(resource, TexturePack.class.getResourceAsStream(name + ".mcmeta"), this.name));
    }
}
