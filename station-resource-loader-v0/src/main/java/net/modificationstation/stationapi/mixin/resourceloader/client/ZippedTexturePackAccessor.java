package net.modificationstation.stationapi.mixin.resourceloader.client;

import net.minecraft.client.resource.ZippedTexturePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.zip.ZipFile;

@Mixin(ZippedTexturePack.class)
public interface ZippedTexturePackAccessor {

    @Accessor
    ZipFile getZipFile();
}
