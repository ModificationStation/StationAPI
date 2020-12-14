package net.modificationstation.stationloader.impl.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@Environment(EnvType.CLIENT)
public class StaticTexture extends Texture {

    private boolean prevRender3d;
    private int[] pixels;

    public StaticTexture(TextureRegistry textureRegistry, String pathToImage) {
        super(textureRegistry, pathToImage);
    }

    @Override
    protected void setImage(BufferedImage image, int targetWidth, int targetHeight, int width, int height) {
        pixels = new int[targetWidth * targetHeight];
        grid = new byte[targetWidth * targetHeight * 4];
        if (width != height || width != targetWidth) {
            BufferedImage image1 = new BufferedImage(targetWidth, targetHeight, 6);
            Graphics2D graphics2d = image1.createGraphics();
            graphics2d.drawImage(image, 0, 0, targetWidth, targetHeight, 0, 0, width, height, null);
            image1.getRGB(0, 0, targetWidth, targetHeight, pixels, 0, targetWidth);
            graphics2d.dispose();
        } else
            image.getRGB(0, 0, width, height, pixels, 0, width);
        update();
    }

    public void update() {
        for (int i = 0; i < pixels.length; i++) {
            int j = pixels[i] >> 24 & 0xff;
            int k = pixels[i] >> 16 & 0xff;
            int l = pixels[i] >> 8 & 0xff;
            int i1 = pixels[i] & 0xff;
            if (render3d) {
                int j1 = (k + l + i1) / 3;
                k = l = i1 = j1;
            }
            grid[i * 4] = (byte) k;
            grid[i * 4 + 1] = (byte) l;
            grid[i * 4 + 2] = (byte) i1;
            grid[i * 4 + 3] = (byte) j;
        }

        prevRender3d = render3d;
    }

    @Override
    public void setup() {
        if (prevRender3d != render3d)
            update();
    }
}
