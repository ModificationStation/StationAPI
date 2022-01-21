package net.modificationstation.stationapi.api.client.texture.atlas;

import com.google.common.collect.ImmutableList;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.resource.Resource;
import net.modificationstation.stationapi.api.client.texture.TextureAnimationData;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.binder.AnimationTextureBinder;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.impl.client.texture.StationRenderAPI;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;
import org.jetbrains.annotations.ApiStatus;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.Tuple;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public class ExpandableAtlas extends Atlas {

    public static final Identifier MISSING = Identifier.of(StationRenderAPI.MODID, "missing");

    private static final Map<String, ExpandableAtlas> PATH_TO_ATLAS = new HashMap<>();

    @ApiStatus.Internal
    private final Map<Identifier, BufferedImage> otherSpritesheetLookup = new IdentityHashMap<>();

    public ExpandableAtlas(final Identifier identifier) {
        super(identifier, "/assets/" + MODID + "/atlases/" + identifier, 0, false);
    }

    public ExpandableAtlas(final Identifier identifier, final Atlas parent) {
        super(identifier, "/assets/" + MODID + "/atlases/" + identifier, 0, false, parent);
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

    public Sprite addTexture(Identifier identifier) {
        return addTexture(identifier, true);
    }

    private Sprite addTexture(Identifier identifier, boolean incrementSize) {
        if (idToTex.containsKey(identifier))
            return idToTex.get(identifier);
        else {
            String texturePath = ResourceManager.ASSETS.toPath(identifier, MODID + "/textures", "png");
            Resource textureResource = Resource.of(TextureHelper.getTextureStream(texturePath));
            BufferedImage image = TextureHelper.readTextureStream(textureResource.getResource());
            Optional<TextureAnimationData> animationDataOptional = TextureAnimationData.parse(textureResource);
            boolean animationPresent = animationDataOptional.isPresent();
            int width = image.getWidth();
            int height = image.getHeight();
            if (animationPresent)
                //noinspection SuspiciousNameCombination
                height = width;
            FileSprite texture = new FileSprite(identifier, texturePath, size, width, height);
            idToTex.put(identifier, texture);
            textures.put(size, texture);
            if (animationPresent)
                addTextureBinder(texture, texture1 -> new AnimationTextureBinder(image, texture1, animationDataOptional.get()));
            if (incrementSize)
                size++;
            return texture;
        }
    }

    @Deprecated
    public Sprite addTexture(String texturePath) {
        return addTexture(ResourceManager.ASSETS.toId(texturePath, "/" + MODID + "/textures", "png"));
    }

    @ApiStatus.Internal
    public ImmutableList<Sprite> addSpritesheet(Identifier atlas, int texturesPerLine, SpritesheetHelper spritesheetHelper) {
        return addSpritesheet(ResourceManager.ASSETS.toPath(atlas, MODID + "/atlases", "png"), texturesPerLine, spritesheetHelper);
    }

    @ApiStatus.Internal
    public ImmutableList<Sprite> addSpritesheet(String pathToAtlas, int texturesPerLine, SpritesheetHelper spritesheetHelper) {
        BufferedImage atlas = TextureHelper.getTexture(pathToAtlas);
        if (((float) atlas.getWidth() / texturesPerLine) % 1 != 0 && ((float) atlas.getHeight() / texturesPerLine) % 1 != 0)
            throw new IllegalStateException("Atlas \"" + pathToAtlas + "\" (" + atlas.getWidth() + "x" + atlas.getHeight() + " doesn't match the textures per line (" + texturesPerLine + ")!");
        ImmutableList.Builder<Sprite> builder = ImmutableList.builder();
        int textureResolution = atlas.getWidth() / texturesPerLine;
        for (int y = 0; y < texturesPerLine; y++) for (int x = 0; x < texturesPerLine; x++) {
            Identifier identifier = spritesheetHelper.generateIdentifier(size);
            if (identifier != null) {
                String texturePath = ResourceManager.ASSETS.toPath(identifier, MODID + "/textures", "png");
                Resource textureResource = Resource.of(TextureHelper.getTextureStream(texturePath));
                Sprite sprite;
                if (textureResource.getResource() == null) {
                    sprite = new FileSprite(identifier, null, size, textureResolution, textureResolution);
                    BiTuple<Integer, Integer> resolution = spritesheetHelper.getResolutionMultiplier(size).map((widthMul, heightMul) -> Tuple.tuple(textureResolution * widthMul, textureResolution * heightMul));
                    textures.put(size, sprite);
                    otherSpritesheetLookup.put(identifier, atlas.getSubimage(x * textureResolution, y * textureResolution, resolution.one(), resolution.two()));
                } else
                    sprite = addTexture(identifier, false);
                builder.add(sprite);
                if (!idToTex.containsKey(identifier))
                    idToTex.put(identifier, sprite);
            }
            size++;
        }
        return builder.build();
    }

    @ApiStatus.Internal
    public void stitch() {
        textures.defaultReturnValue(getMissingTexture());
        TextureStitcher textureStitcher = new TextureStitcher(Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        textures.values().stream().map(sprite -> (FileSprite) sprite).forEach(textureStitcher::add);
        textureStitcher.stitch();
        textureStitcher.getStitchedSprites((spriteInfo, width, height, x, y) -> {
            spriteInfo.x = x;
            spriteInfo.y = y;
            if (imageCache == null)
                imageCache = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            BufferedImage spriteTexture = spriteInfo.path == null ? otherSpritesheetLookup.get(spriteInfo.id.texture) : TextureHelper.getTexture(spriteInfo.path);
            spriteTexture.getSubimage(0, 0, spriteInfo.width, spriteInfo.height);
            Graphics2D graphics = imageCache.createGraphics();
            graphics.drawImage(spriteTexture.getSubimage(0, 0, spriteInfo.width, spriteInfo.height), x, y, null);
            graphics.dispose();
        });
        textures.values().forEach(Sprite::updateUVs);
        refreshTextureID();
    }

    @Override
    protected Sprite getMissingTexture() {
        return addTexture(MISSING);
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



    public static ExpandableAtlas getByPath(String spritesheet) {
        return PATH_TO_ATLAS.get(spritesheet);
    }

    public class FileSprite extends Sprite {

        public final String path;

        protected FileSprite(Identifier id, String path, int index, int width, int height) {
            super(id, index, width, height);
            this.path = path;
        }
    }
}
