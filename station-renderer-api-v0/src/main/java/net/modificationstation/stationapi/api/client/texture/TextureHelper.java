package net.modificationstation.stationapi.api.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;

import java.awt.*;
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
        return ((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePacks.selected.getResource(path);
    }

    public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        var scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var graphics = scaledImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(image, 0, 0, width, height, null);
        return scaledImage;
    }
}
