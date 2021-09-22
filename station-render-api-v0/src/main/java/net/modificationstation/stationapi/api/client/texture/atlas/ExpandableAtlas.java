package net.modificationstation.stationapi.api.client.texture.atlas;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    protected final Map<Identifier, Texture> textureCache = new HashMap<>();

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
        if (textureCache.containsKey(texture))
            return textureCache.get(texture);
        else {
            String texturePath = ResourceManager.parsePath(texture, "/" + MODID + "/textures", "png");
            Resource textureResource = Resource.of(TextureHelper.getTextureStream(texturePath));
            BufferedImage image = TextureHelper.readTextureStream(textureResource.getResource());
            int width = image.getWidth();
            int height = image.getHeight();
            int previousAtlasWidth = imageCache == null ? 0 : imageCache.getWidth();
            Optional<TextureAnimationData> animationDataOptional = parseTextureMeta(textureResource);
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
            FileTexture textureInst = new FileTexture(
                    texturePath, size++,
                    previousAtlasWidth, 0,
                    width, height
            );
            textureCache.put(texture, textureInst);
            textures.add(textureInst);
            if (animationPresent) {
                BufferedImage finalFrames = frames;
                addTextureBinder(textureInst, texture1 -> new AnimationTextureBinder(finalFrames, texture1, animationDataOptional.get()));
            }
            return textureInst;
        }
    }

    protected Optional<TextureAnimationData> parseTextureMeta(Resource resource) {
        if (resource.getMeta().isPresent()) {
            InputStream inputStream = resource.getMeta().get();
            JsonElement tmp = JsonParser.parseReader(new InputStreamReader(inputStream));
            if (tmp.isJsonObject()) {
                JsonObject meta = tmp.getAsJsonObject();
                if (meta.has("animation")) {
                    JsonObject animation = meta.getAsJsonObject("animation");
                    int frametime = animation.has("frametime") ? animation.getAsJsonPrimitive("frametime").getAsInt() : 1;
                    ImmutableList.Builder<TextureAnimationData.Frame> frames = ImmutableList.builder();
                    if (animation.has("frames")) {
                        for (JsonElement element : animation.getAsJsonArray("frames")) {
                            if (element.isJsonPrimitive())
                                frames.add(new TextureAnimationData.Frame(element.getAsInt(), frametime));
                            else if (element.isJsonObject()) {
                                JsonObject frame = element.getAsJsonObject();
                                frames.add(new TextureAnimationData.Frame(frame.getAsJsonPrimitive("index").getAsInt(), frame.has("time") ? frame.getAsJsonPrimitive("time").getAsInt() : frametime));
                            } else
                                throw new RuntimeException("Unknown frame entry: " + element);
                        }
                    }
                    boolean interpolate = animation.has("interpolate") && animation.getAsJsonPrimitive("interpolate").getAsBoolean();
                    return Optional.of(new TextureAnimationData(frametime, frames.build(), interpolate));
                }
            }
        }
        return Optional.empty();
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
            Optional<TextureAnimationData> animationDataOptional = parseTextureMeta(textureResource);
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
