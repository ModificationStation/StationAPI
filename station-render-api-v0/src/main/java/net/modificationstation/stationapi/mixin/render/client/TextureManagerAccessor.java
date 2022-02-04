package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.TexturePackManager;
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
    HashMap<String, Integer> getTextures();

    @Accessor
    ByteBuffer getCurrentImageBuffer();

    @Accessor
    void setCurrentImageBuffer(ByteBuffer byteBuffer);

    @Invoker
    void invokeBindImageToId(BufferedImage bufferedImage, int i);

    @Invoker
    BufferedImage invokeReadImage(InputStream var1);

    @Accessor
    BufferedImage getMissingTexImage();

    @Accessor("isBlurTexture")
    boolean stationapi$isBlurTexture();

    @Accessor("isClampTexture")
    boolean stationapi$isClampTexture();

    @Invoker("method_1098")
    int stationapi$method_1098(int i, int j);

    @Accessor("field_1249")
    IntBuffer stationapi$getField_1249();

    @Accessor("texturePackManager")
    TexturePackManager stationapi$getTexturePackManager();
}
