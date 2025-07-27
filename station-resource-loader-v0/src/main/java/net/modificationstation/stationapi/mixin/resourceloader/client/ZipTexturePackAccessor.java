package net.modificationstation.stationapi.mixin.resourceloader.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.zip.ZipFile;
import net.minecraft.client.resource.pack.ZippedTexturePack;

@Mixin(ZippedTexturePack.class)
public interface ZipTexturePackAccessor {
    @Accessor
    ZipFile getZip();
}
