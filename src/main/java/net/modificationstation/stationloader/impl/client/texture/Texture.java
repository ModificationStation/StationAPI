package net.modificationstation.stationloader.impl.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationloader.api.client.texture.TextureFactory;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Texture extends TextureBinder {

    public Texture(TextureRegistry textureRegistry, String pathToImage) {
        this(textureRegistry, pathToImage, 1);
    }

    public Texture(TextureRegistry textureRegistry, String pathToImage, int textureSize) {
        super(TextureFactory.INSTANCE.nextSpriteID(textureRegistry));
        this.textureRegistry = textureRegistry;
        this.atlasID = index / textureRegistry.texturesPerFile();
        this.index %= textureRegistry.texturesPerFile();
        this.pathToImage = pathToImage;
        this.textureSize = textureSize;
        this.renderMode = textureRegistry.ordinal();
    }

    public final void prepareTexture() throws IOException {
        Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
        bindTexture(mc.textureManager);
        int targetWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, 4096) / textureRegistry.texturesInLine();
        int targetHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, 4097) / textureRegistry.texturesInColumn();
        BufferedImage image = ImageIO.read(mc.texturePackManager.texturePack.method_976(pathToImage));
        int width = image.getWidth();
        int height = image.getHeight();
        setImage(image, targetWidth, targetHeight, width, height);
    }

    protected abstract void setImage(BufferedImage image, int targetWidth, int targetHeight, int width, int height);

    @Override
    public final void bindTexture(TextureManager textureManager) {
        textureRegistry.bindAtlas(textureManager, atlasID);
    }

    protected final int atlasID;
    protected final TextureRegistry textureRegistry;
    protected final String pathToImage;
}
