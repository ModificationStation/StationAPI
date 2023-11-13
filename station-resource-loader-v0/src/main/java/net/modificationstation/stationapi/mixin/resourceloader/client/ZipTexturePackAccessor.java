package net.modificationstation.stationapi.mixin.resourceloader.client;

import net.minecraft.class_592;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.zip.ZipFile;

@Mixin(class_592.class)
public interface ZipTexturePackAccessor {
    @Accessor
    ZipFile getField_2562();
}
