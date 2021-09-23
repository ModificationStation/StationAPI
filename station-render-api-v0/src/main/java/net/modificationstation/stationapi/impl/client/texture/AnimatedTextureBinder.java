package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;

import java.awt.image.*;
import java.io.*;

public class AnimatedTextureBinder extends StationTextureBinder {

    private final String animatedTexture;
    private final int animationRate;
    private byte[][] frames;
    private int currentFrame;
    private int tick = 0;

    public AnimatedTextureBinder(Atlas.Sprite staticReference, String animatedTexture, int animationRate) {
        super(staticReference);
        this.animatedTexture = animatedTexture;
        this.animationRate = animationRate;
        //noinspection deprecation
        reloadFromTexturePack(((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePackManager.texturePack);
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        InputStream stream = newTexturePack.getResourceAsStream(animatedTexture);
        if (stream != null) {
            BufferedImage image = TextureHelper.readTextureStream(stream);
            int
                    targetWidth = getStaticReference().getWidth(),
                    targetHeight = getStaticReference().getHeight(),
                    images = image.getHeight() / targetHeight;
            frames = new byte[images][];
            for (int i = 0; i < images; i++) {
                int[] temp = new int[targetWidth * targetHeight];
                image.getRGB(0, targetHeight * i, targetWidth, targetHeight, temp, 0, targetWidth);
                frames[i] = new byte[targetWidth * targetHeight * 4];
                for (int j = 0; j < temp.length; j++) {
                    int
                            a = temp[j] >> 24 & 0xff,
                            r = temp[j] >> 16 & 0xff,
                            g = temp[j] >> 8 & 0xff,
                            b = temp[j] & 0xff;
                    frames[i][j * 4] = (byte) r;
                    frames[i][j * 4 + 1] = (byte) g;
                    frames[i][j * 4 + 2] = (byte) b;
                    frames[i][j * 4 + 3] = (byte) a;
                }
            }
            grid = frames[currentFrame = 0];
        }
    }

    @Override
    public void update() {
        if (++tick >= animationRate) {
            grid = frames[++currentFrame >= frames.length ? currentFrame = 0 : currentFrame];
            tick = 0;
        }
    }
}