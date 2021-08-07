package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.texture.TextureFactoryOld;
import net.modificationstation.stationapi.api.client.texture.TextureRegistryOld;
import org.lwjgl.opengl.GL11;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

@Deprecated
public abstract class TextureOld extends TextureBinder {

    protected final int atlasID;
    protected final TextureRegistryOld textureRegistry;
    protected final String pathToImage;

    public TextureOld(TextureRegistryOld textureRegistry, String pathToImage) {
        this(textureRegistry, pathToImage, 1);
    }

    public TextureOld(TextureRegistryOld textureRegistry, String pathToImage, int textureSize) {
        super(TextureFactoryOld.INSTANCE.nextSpriteID(textureRegistry));
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
        BufferedImage image = ImageIO.read(mc.texturePackManager.texturePack.getResourceAsStream(pathToImage));
        int width = image.getWidth();
        int height = image.getHeight();
        setImage(image, targetWidth, targetHeight, width, height);
    }

    protected abstract void setImage(BufferedImage image, int targetWidth, int targetHeight, int width, int height);

    @Override
    public final void bindTexture(TextureManager textureManager) {
        textureRegistry.bindAtlas(textureManager, atlasID);
    }
}
