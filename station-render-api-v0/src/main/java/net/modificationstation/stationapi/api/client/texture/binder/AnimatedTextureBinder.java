package net.modificationstation.stationapi.api.client.texture.binder;

import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;

import java.awt.image.*;

public class AnimatedTextureBinder extends StationTextureBinder implements TexturePackDependent {

    private final String animatedTexture;
    private final int animationRate;
    private byte[][] frames;
    private int currentFrame = 0;
    private int tick = 0;

    public AnimatedTextureBinder(Atlas.Texture staticReference, String animatedTexture, int animationRate) {
        super(staticReference);
        this.animatedTexture = animatedTexture;
        this.animationRate = animationRate;
        refreshTextures();
    }

    @Override
    public void refreshTextures() {
        BufferedImage image = TextureHelper.getTexture(animatedTexture);
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
        grid = frames[0];
    }

    @Override
    public void update() {
        if (tick++ >= animationRate) {
            grid = frames[++currentFrame >= frames.length ? currentFrame = 0 : currentFrame];
            tick = 0;
        }
    }
}
