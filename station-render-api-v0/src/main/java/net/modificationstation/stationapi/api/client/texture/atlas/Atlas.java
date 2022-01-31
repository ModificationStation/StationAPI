package net.modificationstation.stationapi.api.client.texture.atlas;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.texture.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.client.texture.TexturePackDependent;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import uk.co.benjiweber.expressions.function.ObjIntFunction;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public abstract class Atlas {

    private static final Function<Atlas, Tessellator> EMPTY_TESSELLATOR = atlas -> null;

    public final Identifier id;
    public final String spritesheet;
    protected final Atlas parent;
    protected int size;
    public final boolean fixedSize;
    protected final Map<Identifier, Sprite> idToTex = new IdentityHashMap<>();
    protected final Int2ObjectMap<Sprite> textures = new Int2ObjectOpenHashMap<>();
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
        do if (!atlas.getTexture(texture).equals(atlas.getMissingTexture()))
            //noinspection unchecked
            return (T) atlas;
        while ((atlas = atlas.parent) != null);
        //noinspection unchecked
        return (T) this;
    }

    public final int getAtlasTextureID() {
        //noinspection deprecation
        return ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager.getTextureId(spritesheet);
    }

    public final void bindAtlas() {
        //noinspection deprecation
        ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager.bindTexture(getAtlasTextureID());
    }

    public final Sprite getTexture(Identifier identifier) {
        return idToTex.get(identifier);
    }

    public final Sprite getTexture(int textureIndex) {
        return textures.get(parent == null ? textureIndex : textureIndex - parent.size);
    }

    protected Sprite getMissingTexture() {
        return null;
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
            if (arg instanceof TexturePackDependent)
                ((TexturePackDependent) arg).reloadFromTexturePack(texturePack);
            textureManager.addTextureBinder(arg);
        });
    }

    public abstract class Sprite {

        @Getter
        protected Identifier id;
        public final int index;
        @Getter
        protected int
                x, y,
                width, height;
        public final AnimationResourceMetadata animationData;

        public Sprite(Identifier id, int index, int width, int height, AnimationResourceMetadata animationData) {
            this.id = id;
            this.index = index;
            this.width = width;
            this.height = height;
            this.animationData = animationData;
        }

        public final Atlas getAtlas() {
            return Atlas.this;
        }

        /* !==========================! */
        /* !--- DEPRECATED SECTION ---! */
        /* !==========================! */

        @Deprecated
        protected Sprite(int index, int x, int y, int width, int height) {
            this(Identifier.of(Atlas.this.id.modID, String.valueOf(index)), index, width, height, AnimationResourceMetadata.EMPTY);
            this.x = x;
            this.y = y;
        }

        public abstract double getStartU();

        public abstract double getEndU();

        public abstract double getStartV();

        public abstract double getEndV();
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
