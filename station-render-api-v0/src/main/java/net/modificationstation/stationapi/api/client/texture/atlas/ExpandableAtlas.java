package net.modificationstation.stationapi.api.client.texture.atlas;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.binder.AnimatedTextureBinder;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;
import uk.co.benjiweber.expressions.collection.EnhancedList;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

public class ExpandableAtlas extends Atlas {

    private static final Map<String, ExpandableAtlas> PATH_TO_ATLAS = new HashMap<>();

    protected final Map<String, Texture> textureCache = new HashMap<>();

    public ExpandableAtlas(final Identifier identifier) {
        super("/assets/stationapi/atlases/" + identifier, 0, false);
    }

    public ExpandableAtlas(final Identifier identifier, final Atlas parent) {
        super("/assets/stationapi/atlases/" + identifier, 0, false, parent);
    }

    @Override
    protected void init() {
        PATH_TO_ATLAS.put(spritesheet, this);
    }

    @Override
    public BufferedImage getImage() {
        return imageCache;
    }

    @Override
    public InputStream getStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            //noinspection deprecation
            ImageIO.write(imageCache == null ? ((TextureManagerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager).getMissingTexImage() : imageCache, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public Texture addTexture(String texture) {
        if (textureCache.containsKey(texture))
            return textureCache.get(texture);
        else {
            BufferedImage image = TextureHelper.getTexture(texture);
            int previousAtlasWidth = imageCache == null ? 0 : imageCache.getWidth();
            drawTextureOnSpritesheet(image);
            textures.forEach(Texture::updateUVs);
            refreshTextureID();
            return textureCache.compute(texture, (s, texture1) -> EnhancedList.enhance(textures).addAndReturn(new FileTexture(
                    texture, size++,
                    previousAtlasWidth, 0,
                    image.getWidth(), image.getHeight()
            )));
        }
    }

    public <T extends StationTextureBinder> T addTextureBinder(String staticReference, Function<Texture, T> initializer) {
        return addTextureBinder(addTexture(staticReference), initializer);
    }

    public AnimatedTextureBinder addAnimationBinder(String animationPath, int animationRate, String staticReference) {
        return addAnimationBinder(animationPath, animationRate, addTexture(staticReference));
    }

    private void drawTextureOnSpritesheet(BufferedImage image) {
        if (imageCache == null) {
            ColorModel cm = image.getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = image.copyData(null);
            imageCache = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        } else {
            int previousAtlasWidth = imageCache.getWidth();
            resizeSpritesheet(imageCache.getWidth() + image.getWidth(), Math.max(image.getHeight(), imageCache.getHeight()));
            Graphics2D graphics = imageCache.createGraphics();
            graphics.drawImage(image, previousAtlasWidth, 0, null);
            graphics.dispose();
        }
    }

    private void resizeSpritesheet(int targetWidth, int targetHeight) {
        BufferedImage previousSpriteSheet = imageCache;
        imageCache = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = imageCache.createGraphics();
        graphics.drawImage(previousSpriteSheet, 0, 0, null);
        graphics.dispose();
    }

    protected void refreshTextureID() {
        if (imageCache != null) {
            //noinspection deprecation
            Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
            TextureManagerAccessor textureManager = (TextureManagerAccessor) minecraft.textureManager;
            textureManager.invokeBindImageToId(imageCache, minecraft.textureManager.getTextureId(spritesheet));
        }
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        super.reloadFromTexturePack(newTexturePack);
        textures.forEach(texture -> {
            texture.x = imageCache == null ? 0 : imageCache.getWidth();
            texture.y = 0;
            BufferedImage image = TextureHelper.readTextureStream(newTexturePack.getResourceAsStream(((FileTexture) texture).path));
            texture.width = image.getWidth();
            texture.height = image.getHeight();
            drawTextureOnSpritesheet(image);
        });
        textures.forEach(Texture::updateUVs);
        refreshTextureID();
    }

    public static ExpandableAtlas getByPath(String spritesheet) {
        return PATH_TO_ATLAS.get(spritesheet);
    }

    public class FileTexture extends Texture {

        public final String path;

        protected FileTexture(String path, int index, int x, int y, int width, int height) {
            super(index, x, y, width, height);
            this.path = path;
        }
    }
}
