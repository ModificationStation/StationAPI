package net.modificationstation.stationapi.api.client.texture.atlas;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.MissingSprite;
import net.modificationstation.stationapi.api.client.texture.TexturePackDependent;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.ApiStatus;
import uk.co.benjiweber.expressions.function.ObjIntFunction;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public abstract class Atlas {

    public final Identifier id;
    public final String spritesheet;
    protected final Atlas parent;
    protected int size;
    public final boolean fixedSize;
    @ApiStatus.Internal
    public final Map<Identifier, Sprite> idToTex = new IdentityHashMap<>();
    private final Sprite missing = new Sprite(MissingSprite.getMissingSpriteId(), -1);
    protected final Int2ObjectMap<Sprite> textures = Util.make(new Int2ObjectOpenHashMap<>(), map -> map.defaultReturnValue(missing));
    public final List<TextureBinder> textureBinders = new CopyOnWriteArrayList<>();

    Atlas(final Identifier id, final String spritesheet, final int size, final boolean fixedSize) {
        this(id, spritesheet, size, fixedSize, null);
    }

    Atlas(final Identifier id, final String spritesheet, final int size, final boolean fixedSize, final Atlas parent) {
        this.id = id;
        this.spritesheet = spritesheet;
        if (parent == null)
            this.size = size;
        else {
            if (parent.fixedSize)
                this.size = parent.size + size;
            else
                throw new UnsupportedOperationException("Parent atlas can't have dynamic size!");
        }
        this.fixedSize = fixedSize;
        this.parent = parent;
    }

    protected final <T> T applyInherited(int textureIndex, IntFunction<T> atlasBounds, ObjIntFunction<Atlas, T> parentBounds) {
        if (parent == null) {
            if (0 <= textureIndex && textureIndex < size)
                return atlasBounds.apply(textureIndex);
        } else {
            if (textureIndex < parent.size)
                return parentBounds.apply(parent, textureIndex);
            else if (textureIndex < size)
                return atlasBounds.apply(textureIndex - parent.size);
        }
        throw new IllegalArgumentException("Texture index " + textureIndex + " out of bounds of " + spritesheet + " atlas!");
    }

    public final <T extends Atlas> T of(int textureIndex) {
        //noinspection unchecked
        return (T) applyInherited(textureIndex, value -> this, Atlas::of);
    }

    public final <T extends Atlas> T of(Identifier texture) {
        Atlas atlas = this;
        do if (atlas.getTexture(texture).id != atlas.getMissingTexture().id)
            //noinspection unchecked
            return (T) atlas;
        while ((atlas = atlas.parent) != null);
        //noinspection unchecked
        return (T) this;
    }

    public final int getAtlasTextureID() {
        return StationRenderAPI.BAKED_MODEL_MANAGER.getAtlas(Atlases.BLOCK_ATLAS_TEXTURE).getGlId();
    }

    public final void bindAtlas() {
        StationRenderAPI.BAKED_MODEL_MANAGER.getAtlas(Atlases.BLOCK_ATLAS_TEXTURE).bindTexture();
    }

    public final Sprite getTexture(Identifier identifier) {
        return of(identifier).idToTex.get(identifier);
    }

    public final Sprite getTexture(int textureIndex) {
        return applyInherited(textureIndex, textures::get, Atlas::getTexture);
    }

    protected Sprite getMissingTexture() {
        return missing;
    }

    public final <T extends StationTextureBinder> T addTextureBinder(int staticReferenceTextureIndex, Function<Sprite, T> initializer) {
        return addTextureBinder(getTexture(staticReferenceTextureIndex), initializer);
    }

    public final <T extends StationTextureBinder> T addTextureBinder(Sprite staticReference, Function<Sprite, T> initializer) {
        T textureBinder = initializer.apply(staticReference);
        textureBinders.add(textureBinder);
        return textureBinder;
    }

    public void registerTextureBinders(TextureManager textureManager, TexturePack texturePack) {
        textureBinders.forEach(arg -> {
            if (arg instanceof TexturePackDependent dependent)
                dependent.reloadFromTexturePack(texturePack);
            textureManager.addTextureBinder(arg);
        });
    }

    public class Sprite {

        @Getter
        protected Identifier id;
        public final int index;

        public Sprite(Identifier id, int index) {
            this.id = id;
            this.index = index;
        }

        public final Atlas getAtlas() {
            return Atlas.this;
        }

        public int getX() {
            net.modificationstation.stationapi.api.client.texture.Sprite sprite = getSprite();
            return (int) (sprite.getMinU() * sprite.getWidth() / (sprite.getMaxU() - sprite.getMinU()));
        }

        public int getY() {
            net.modificationstation.stationapi.api.client.texture.Sprite sprite = getSprite();
            return (int) (sprite.getMinV() * sprite.getHeight() / (sprite.getMaxV() - sprite.getMinV()));
        }

        public int getWidth() {
            return getSprite().getWidth();
        }

        public int getHeight() {
            return getSprite().getHeight();
        }

        public double getStartU() {
            return getSprite().getMinU();
        }

        public double getEndU() {
            return getSprite().getMaxU();
        }

        public double getStartV() {
            return getSprite().getMinV();
        }

        public double getEndV() {
            return getSprite().getMaxV();
        }

        public net.modificationstation.stationapi.api.client.texture.Sprite getSprite() {
            return StationRenderAPI.BAKED_MODEL_MANAGER.getAtlas(Atlases.BLOCK_ATLAS_TEXTURE).getSprite(id);
        }

        /* !==========================! */
        /* !--- DEPRECATED SECTION ---! */
        /* !==========================! */

        @Deprecated
        protected Sprite(int index, int x, int y, int width, int height) {
            this(Identifier.of(Atlas.this.id.modID, String.valueOf(index)), index);
        }
    }

    /* !==========================! */
    /* !--- DEPRECATED SECTION ---! */
    /* !==========================! */

    @Deprecated
    private static Identifier calcId(String spritesheet) {
        LOGGER.warn("Using a deprecated atlas initializer on spritesheet \"" + spritesheet + "\"! Attempting to calculate the identifier...");
        if (ResourceManager.ASSETS.contains(spritesheet))
            try {
                return ResourceManager.ASSETS.toId(spritesheet, "", "png");
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Atlas spritesheet path doesn't seem to follow /assets/modid/ format.", e);
            }
        LOGGER.warn("Modid calculation failed, generating an identifier under \"minecraft\" modid and the full spritesheet path as the id.");
        return Identifier.of(spritesheet.substring(1));
    }

    @Deprecated
    public Atlas(final String spritesheet, final int size, final boolean fixedSize) {
        this(calcId(spritesheet), spritesheet, size, fixedSize);
    }

    @Deprecated
    public Atlas(final String spritesheet, final int size, final boolean fixedSize, final Atlas parent) {
        this(calcId(spritesheet), spritesheet, size, fixedSize, parent);
    }
}
