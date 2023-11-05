package net.modificationstation.stationapi.api.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public class TextureHelper {

    public static BufferedImage getTexture(String path) {
        return readTextureStream(getTextureStream(path));
    }

    public static BufferedImage readTextureStream(InputStream stream) {
        //noinspection deprecation
        return ((TextureManagerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager).invokeMethod_1091(stream);
    }

    public static InputStream getTextureStream(String path) {
        //noinspection deprecation
        return ((Minecraft) FabricLoader.getInstance().getGameInstance()).field_2768.field_1175.method_976(path);
    }
}
