package net.modificationstation.stationapi.api.client.texture.atlas;

import com.google.common.collect.ImmutableList;
import net.modificationstation.stationapi.api.client.resource.Resource;
import net.modificationstation.stationapi.api.client.texture.SpritesheetHelper;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.Tuple;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public class ExpandableAtlas extends Atlas {

    protected final Map<Identifier, BufferedImage> slicedSpritesheetSprites = new IdentityHashMap<>();
    public final Map<Identifier, BufferedImage> slicedSpritesheetView = Collections.unmodifiableMap(slicedSpritesheetSprites);

    public ExpandableAtlas(final Identifier identifier) {
        super(identifier, "/assets/" + MODID + "/atlases/" + identifier, 0, false);
    }

    public ExpandableAtlas(final Identifier identifier, final Atlas parent) {
        super(identifier, "/assets/" + MODID + "/atlases/" + identifier, 0, false, parent);
    }

    public Sprite addTexture(Identifier identifier) {
        return addTexture(identifier, true);
    }

    private Sprite addTexture(Identifier identifier, boolean incrementSize) {
        if (idToTex.containsKey(identifier))
            return idToTex.get(identifier);
        else {
            String texturePath = ResourceManager.ASSETS.toPath(identifier, MODID + "/textures", "png");
            FileSprite texture = new FileSprite(identifier, texturePath, size);
            idToTex.put(identifier, texture);
            textures.put(size, texture);
            if (incrementSize)
                size++;
            return texture;
        }
    }

    @Deprecated
    public Sprite addTexture(String texturePath) {
        return addTexture(ResourceManager.ASSETS.toId(texturePath, "/" + MODID + "/textures", "png"));
    }

    public ImmutableList<Sprite> addSpritesheet(Identifier atlas, int texturesPerLine, SpritesheetHelper spritesheetHelper) {
        return addSpritesheet(ResourceManager.ASSETS.toPath(atlas, MODID + "/atlases", "png"), texturesPerLine, spritesheetHelper);
    }

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
                if (textureResource.getInputStream() == null) {
                    BiTuple<Integer, Integer> resolution = spritesheetHelper.getResolutionMultiplier(size).map((widthMul, heightMul) -> Tuple.tuple(textureResolution * widthMul, textureResolution * heightMul));
                    sprite = new FileSprite(identifier, null, size);
                    textures.put(size, sprite);
                    BufferedImage spriteImage = atlas.getSubimage(x * textureResolution, y * textureResolution, resolution.one(), resolution.two());
                    slicedSpritesheetSprites.put(identifier, spriteImage);
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
//
//    public net.modificationstation.stationapi.api.client.texture.Sprite getSprite(Identifier identifier) {
//        return sprites.get(identifier);
//    }
//
//    @Override
//    protected Sprite getMissingTexture() {
//        return addTexture(MISSING);
//    }

    public <T extends StationTextureBinder> T addTextureBinder(Identifier staticReference, Function<Sprite, T> initializer) {
        return addTextureBinder(addTexture(staticReference), initializer);
    }

    public class FileSprite extends Sprite {

        public final String path;

        protected FileSprite(Identifier id, String path, int index) {
            super(id, index);
            this.path = path;
        }
    }
}
