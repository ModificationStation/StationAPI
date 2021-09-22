package net.modificationstation.stationapi.api.client.texture.atlas;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.resource.Resource;
import net.modificationstation.stationapi.api.client.texture.TextureAnimationData;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.binder.AnimationTextureBinder;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

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

    public Texture addTexture(Identifier texture) {
        return addTexture(ResourceManager.parsePath(texture, "/" + MODID + "/textures", "png"));
    }

    public Texture addTexture(String texturePath) {
        if (textureCache.containsKey(texturePath))
            return textureCache.get(texturePath);
        else {
            Resource textureResource = Resource.of(TextureHelper.getTextureStream(texturePath));
            BufferedImage image = TextureHelper.readTextureStream(textureResource.getResource());
            int width = image.getWidth();
            int height = image.getHeight();
            int previousAtlasWidth = imageCache == null ? 0 : imageCache.getWidth();
            Optional<TextureAnimationData> animationDataOptional = TextureAnimationData.parse(textureResource);
            boolean animationPresent = animationDataOptional.isPresent();
            BufferedImage frames = null;
            if (animationPresent) {
                //noinspection SuspiciousNameCombination
                height = width;
                frames = image;
                image = image.getSubimage(0, 0, width, height);
            }
            drawTextureOnSpritesheet(image);
            refreshTextureID();
            textures.forEach(Texture::updateUVs);
            FileTexture texture = new FileTexture(
                    texturePath, size++,
                    previousAtlasWidth, 0,
                    width, height
            );
            textureCache.put(texturePath, texture);
            textures.add(texture);
            if (animationPresent) {
                BufferedImage finalFrames = frames;
                addTextureBinder(texture, texture1 -> new AnimationTextureBinder(finalFrames, texture1, animationDataOptional.get()));
            }
            return texture;
        }
    }

    public <T extends StationTextureBinder> T addTextureBinder(Identifier staticReference, Function<Texture, T> initializer) {
        return addTextureBinder(addTexture(staticReference), initializer);
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
            Resource textureResource = Resource.of(newTexturePack.getResourceAsStream(((FileTexture) texture).path));
            BufferedImage image = TextureHelper.readTextureStream(textureResource.getResource());
            int
                    width = image.getWidth(),
                    height = image.getHeight();
            Optional<TextureAnimationData> animationDataOptional = TextureAnimationData.parse(textureResource);
            BufferedImage frames = null;
            boolean animationPresent = animationDataOptional.isPresent();
            if (animationPresent) {
                //noinspection SuspiciousNameCombination
                height = width;
                frames = image;
                image = image.getSubimage(0, 0, width, height);
            }
            texture.width = width;
            texture.height = height;
            drawTextureOnSpritesheet(image);
            if (animationPresent) {
                TextureAnimationData animationData = animationDataOptional.get();
                //noinspection deprecation
                TextureManager textureManager = ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager;
                textureManager.addTextureBinder(new AnimationTextureBinder(frames, texture, animationData));
            }
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
