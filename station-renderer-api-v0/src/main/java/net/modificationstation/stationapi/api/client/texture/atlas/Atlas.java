package net.modificationstation.stationapi.api.client.texture.atlas;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import net.minecraft.class_285;
import net.minecraft.class_336;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.MissingSprite;
import net.modificationstation.stationapi.api.client.texture.TexturePackDependent;
import net.modificationstation.stationapi.api.client.texture.binder.StaticReferenceProvider;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.ApiStatus;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.IntFunction;

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
    public final List<class_336> textureBinders = new CopyOnWriteArrayList<>();

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

    @FunctionalInterface
    protected interface ParentBoundsGetter<T> {
        T getBounds(Atlas atlas, int texture);
    }

    protected final <T> T applyInherited(int textureIndex, IntFunction<T> atlasBounds, ParentBoundsGetter<T> parentBounds) {
        if (parent == null) {
            if (0 <= textureIndex && textureIndex < size)
                return atlasBounds.apply(textureIndex);
        } else {
            if (textureIndex < parent.size)
                return parentBounds.getBounds(parent, textureIndex);
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
        do if (atlas.idToTex.containsKey(texture))
            //noinspection unchecked
            return (T) atlas;
        while ((atlas = atlas.parent) != null);
        //noinspection unchecked
        return (T) this;
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

    public void registerTextureBinders(TextureManager textureManager, class_285 texturePack) {
        textureBinders.stream().filter(textureBinder -> !(textureBinder instanceof StaticReferenceProvider provider) || provider.getStaticReference().getSprite().getContents().getAnimation() == null).forEach(arg -> {
            if (arg instanceof TexturePackDependent dependent)
                dependent.reloadFromTexturePack(texturePack);
            textureManager.method_1087(arg);
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
            return getSprite().getX();
        }

        public int getY() {
            return getSprite().getY();
        }

        public int getWidth() {
            return getSprite().getContents().getWidth();
        }

        public int getHeight() {
            return getSprite().getContents().getHeight();
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
            return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getSprite(id);
        }
    }
}
