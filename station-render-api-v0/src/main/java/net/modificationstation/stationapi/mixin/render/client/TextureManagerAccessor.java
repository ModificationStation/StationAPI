package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.TextureBinder;
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
    GameOptions getGameOptions();

    @Accessor
    List<TextureBinder> getTextureBinders();

    @Accessor
    HashMap<String, Integer> getField_1246();

    @Accessor
    ByteBuffer getField_1250();

    @Accessor
    void setField_1250(ByteBuffer byteBuffer);

    @Invoker
    void invokeMethod_1089(BufferedImage bufferedImage, int i);

    @Invoker
    BufferedImage invokeMethod_1091(InputStream var1);
}
