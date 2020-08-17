package net.modificationstation.stationloader.impl.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureBinder;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public class StaticTexture extends TextureBinder {

    public StaticTexture(TextureRegistry type, String pathToImage) {
        this(type, 1, pathToImage);
    }

    public StaticTexture(TextureRegistry type, int size, String pathToImage) {
        super(TextureFactory.nextSpriteID(type));
        this.type = type;
        atlasID = index / this.type.texturesPerFile();
        index %= this.type.texturesPerFile();
        this.pathToImage = pathToImage;
        pixels = null;
        textureSize = size;
        renderMode = this.type.ordinal();
    }

    public void prepareTexture() throws IOException {
        Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
        this.bindTexture(mc.textureManager);
        int l = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, 4096 /*GL_TEXTURE_WIDTH*/) / 16;
        int i1 = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D /*GL_TEXTURE_2D*/, 0, 4097 /*GL_TEXTURE_HEIGHT*/) / 16;
        BufferedImage bufferedimage = ImageIO.read(mc.texturePackManager.texturePack.method_976(pathToImage));
        int j1 = bufferedimage.getWidth();
        int k1 = bufferedimage.getHeight();
        pixels = new int[l * i1];
        grid = new byte[l * i1 * 4];
        if(j1 != k1 || j1 != l) {
            BufferedImage bufferedimage1 = new BufferedImage(l, i1, 6);
            Graphics2D graphics2d = bufferedimage1.createGraphics();
            graphics2d.drawImage(bufferedimage, 0, 0, l, i1, 0, 0, j1, k1, null);
            bufferedimage1.getRGB(0, 0, l, i1, pixels, 0, l);
            graphics2d.dispose();
        } else
            bufferedimage.getRGB(0, 0, j1, k1, pixels, 0, j1);
        update();
    }

    public void update() {
        for(int i = 0; i < pixels.length; i++) {
            int j = pixels[i] >> 24 & 0xff;
            int k = pixels[i] >> 16 & 0xff;
            int l = pixels[i] >> 8 & 0xff;
            int i1 = pixels[i] & 0xff;
            if(render3d) {
                int j1 = (k + l + i1) / 3;
                k = l = i1 = j1;
            }
            grid[i * 4] = (byte)k;
            grid[i * 4 + 1] = (byte)l;
            grid[i * 4 + 2] = (byte)i1;
            grid[i * 4 + 3] = (byte)j;
        }

        prevRender3d = render3d;
    }

    @Override
    public void setup() {
        if(prevRender3d != render3d)
            update();
    }

    @Override
    public void bindTexture(net.minecraft.client.texture.TextureManager textureManager) {
        type.bindAtlas(textureManager, atlasID);
    }

    private final TextureRegistry type;
    public final int atlasID;
    private final String pathToImage;
    private boolean prevRender3d;
    private int[] pixels;
}
