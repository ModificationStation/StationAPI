package net.modificationstation.stationloader.mixin.client;

import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.HashMap;

@Mixin(TextureManager.class)
public interface TextureManagerAccessor {

    @Accessor
    @SuppressWarnings("rawtypes")
    HashMap getField_1246();
    @Accessor
    IntBuffer getField_1249();
    @Accessor
    BufferedImage getField_1257();
    @Invoker
    BufferedImage invokeMethod_1091(InputStream var1);
}
