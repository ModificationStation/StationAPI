package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.texture.DynamicTexture;
import net.minecraft.client.resource.pack.TexturePacks;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

@Mixin(TextureManager.class)
public interface TextureManagerAccessor {
    @Accessor
    GameOptions getGameOptions();

    @Accessor
    List<DynamicTexture> getDynamicTextures();

    @Accessor
    HashMap<String, Integer> getTextures();

    @Accessor
    ByteBuffer getImageBuffer();

    @Accessor
    void setImageBuffer(ByteBuffer byteBuffer);

    @Invoker
    BufferedImage invokeReadImage(InputStream var1);

    @Accessor("blur")
    boolean stationapi$isBlurTexture();

    @Accessor("clamp")
    boolean stationapi$isClampTexture();

    @Invoker("crispBlend")
    int stationapi$crispBlend(int i, int j);

    @Accessor("texturePacks")
    TexturePacks stationapi$getTexturePackManager();
}
