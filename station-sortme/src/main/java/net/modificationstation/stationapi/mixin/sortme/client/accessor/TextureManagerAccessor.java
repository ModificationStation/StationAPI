package net.modificationstation.stationapi.mixin.sortme.client.accessor;

import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.util.*;

@Mixin(TextureManager.class)
public interface TextureManagerAccessor {

    @Accessor
    HashMap<String, Integer> getField_1246();

    @Accessor
    IntBuffer getField_1249();

    @Accessor
    BufferedImage getField_1257();

    @Invoker
    BufferedImage invokeMethod_1091(InputStream var1);

    @Invoker
    void invokeMethod_1089(BufferedImage bufferedImage, int i);
}
