package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;

@Environment(EnvType.CLIENT)
public class AnimatedTexture extends Texture {

    private final int tickRate;
    private byte[][] images;
    private int index;
    private int ticks;
    public AnimatedTexture(TextureRegistry textureRegistry, String pathToImage, int animationRate) {
        super(textureRegistry, pathToImage);
        index = 0;
        tickRate = animationRate;
        ticks = animationRate;
    }

    @Override
    public void setImage(BufferedImage image, int targetWidth, int targetHeight, int width, int height) {
        int images = height / width;
        if (images <= 0)
            throw new IllegalArgumentException("source has no complete images");
        this.images = new byte[images][];
        if (width != targetWidth) {
            BufferedImage img = new BufferedImage(targetWidth, targetHeight * images, 6);
            Graphics2D gfx = img.createGraphics();
            gfx.drawImage(image, 0, 0, targetWidth, targetHeight * images, 0, 0, width, height, null);
            gfx.dispose();
            image = img;
        }
        for (int i = 0; i < images; i++) {
            int[] temp = new int[targetWidth * targetHeight];
            image.getRGB(0, targetHeight * i, targetWidth, targetHeight, temp, 0, targetWidth);
            this.images[i] = new byte[targetWidth * targetHeight * 4];
            for (int j = 0; j < temp.length; j++) {
                int a = temp[j] >> 24 & 0xff;
                int r = temp[j] >> 16 & 0xff;
                int g = temp[j] >> 8 & 0xff;
                int b = temp[j] & 0xff;
                this.images[i][j * 4] = (byte) r;
                this.images[i][j * 4 + 1] = (byte) g;
                this.images[i][j * 4 + 2] = (byte) b;
                this.images[i][j * 4 + 3] = (byte) a;
            }
        }
    }

    @Override
    public void update() {
        if (ticks >= tickRate) {
            index++;
            if (index >= images.length)
                index = 0;
            grid = images[index];
            ticks = 0;
        }
        ticks++;
    }
}
