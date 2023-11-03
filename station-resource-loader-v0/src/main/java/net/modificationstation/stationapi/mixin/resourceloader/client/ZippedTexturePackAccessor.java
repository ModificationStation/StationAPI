package net.modificationstation.stationapi.mixin.resourceloader.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.zip.ZipFile;
import net.minecraft.class_592;

@Mixin(class_592.class)
public interface ZippedTexturePackAccessor {

    @Accessor
    ZipFile getZipFile();
}
