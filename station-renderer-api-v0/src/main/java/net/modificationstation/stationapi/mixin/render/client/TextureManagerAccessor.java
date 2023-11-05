package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.class_303;
import net.minecraft.class_336;
import net.minecraft.client.option.GameOptions;
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
    List<class_336> getField_1251();

    @Accessor
    HashMap<String, Integer> getTextures();

    @Accessor
    ByteBuffer getField_1250();

    @Accessor
    void setField_1250(ByteBuffer byteBuffer);

    @Invoker
    BufferedImage invokeMethod_1091(InputStream var1);

    @Accessor("field_1255")
    boolean stationapi$isBlurTexture();

    @Accessor("field_1254")
    boolean stationapi$isClampTexture();

    @Invoker("method_1098")
    int stationapi$method_1098(int i, int j);

    @Accessor("field_1256")
    class_303 stationapi$getTexturePackManager();
}
