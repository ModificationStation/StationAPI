package net.modificationstation.stationapi.api.client.texture.atlas;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.resource.Resource;
import net.modificationstation.stationapi.api.client.texture.TextureAnimationData;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.binder.AnimationTextureBinder;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;
import org.jetbrains.annotations.ApiStatus;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.function.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public class ExpandableAtlas extends Atlas {

    private static final boolean DEBUG_EXPORT_ATLASES = Boolean.parseBoolean(System.getProperty(MODID + ".debug.export_atlases", "false"));

    private static final Map<String, ExpandableAtlas> PATH_TO_ATLAS = new HashMap<>();

    public final Identifier id;
    protected final Map<String, Sprite> textureCache = new HashMap<>();

    public ExpandableAtlas(final Identifier identifier) {
        super("/assets/" + MODID + "/atlases/" + identifier, 0, false);
        id = identifier;
    }

    public ExpandableAtlas(final Identifier identifier, final Atlas parent) {
        super("/assets/" + MODID + "/atlases/" + identifier, 0, false, parent);
        id = identifier;
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

    public Sprite addTexture(Identifier texture) {
        return addTexture(ResourceManager.ASSETS.toPath(texture, MODID + "/textures", "png"));
    }

    public Sprite addTexture(String texturePath) {
        return addTexture(texturePath, true);
    }

    private Sprite addTexture(String texturePath, boolean stitch) {
        if (textureCache.containsKey(texturePath))
            return textureCache.get(texturePath);
        else {
            Resource textureResource = Resource.of(TextureHelper.getTextureStream(texturePath));
            BufferedImage image = TextureHelper.readTextureStream(textureResource.getResource());
            Optional<TextureAnimationData> animationDataOptional = TextureAnimationData.parse(textureResource);
            boolean animationPresent = animationDataOptional.isPresent();
            int width = image.getWidth();
            int height = image.getHeight();
            if (animationPresent)
                //noinspection SuspiciousNameCombination
                height = width;
            // The texture is yet to be stitched on the atlas, so the coordinates are left uninitialized.
            FileSprite texture = new FileSprite(texturePath, size++, -1, -1, width, height);
            textureCache.put(texturePath, texture);
            textures.add(texture);
            if (animationPresent)
                addTextureBinder(texture, texture1 -> new AnimationTextureBinder(image, texture1, animationDataOptional.get()));
            if (stitch) {
                imageCache = null;
                stitch();
            }
            return texture;
        }
    }

    @ApiStatus.Internal
    public void stitch() {
        TextureStitcher textureStitcher = new TextureStitcher(Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        textures.stream().map(sprite -> (FileSprite) sprite).forEach(textureStitcher::add);
        textureStitcher.stitch();
        textureStitcher.getStitchedSprites((spriteInfo, width, height, x, y) -> {
            spriteInfo.x = x;
            spriteInfo.y = y;
            if (imageCache == null)
                imageCache = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            BufferedImage spriteTexture = TextureHelper.getTexture(spriteInfo.path);
            spriteTexture.getSubimage(0, 0, spriteInfo.width, spriteInfo.height);
            Graphics2D graphics = imageCache.createGraphics();
            graphics.drawImage(TextureHelper.getTexture(spriteInfo.path).getSubimage(0, 0, spriteInfo.width, spriteInfo.height), x, y, null);
            graphics.dispose();
        });
        textures.forEach(Sprite::updateUVs);
        refreshTextureID();
        if (DEBUG_EXPORT_ATLASES)
            try {
                ImageIO.write(imageCache, "png", new File("debug/exported_atlases/" + id.toString().replace(":", "_") + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    public <T extends StationTextureBinder> T addTextureBinder(Identifier staticReference, Function<Sprite, T> initializer) {
        return addTextureBinder(addTexture(staticReference), initializer);
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
        List<Sprite> sprites = new ArrayList<>(textures);
        textureCache.clear();
        textures.clear();
        sprites.stream().map(sprite -> (FileSprite) sprite).forEach(sprite -> addTexture(sprite.path, false));
        stitch();
    }

    public static ExpandableAtlas getByPath(String spritesheet) {
        return PATH_TO_ATLAS.get(spritesheet);
    }

    public class FileSprite extends Sprite {

        public final String path;

        protected FileSprite(String path, int index, int x, int y, int width, int height) {
            super(index, x, y, width, height);
            this.path = path;
        }
    }
}
