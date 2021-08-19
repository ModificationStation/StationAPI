package net.modificationstation.stationapi.api.client.texture.atlas;

import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.binder.AnimatedTextureBinder;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;
import uk.co.benjiweber.expressions.function.ObjIntFunction;

import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public abstract class Atlas {

    private static final Set<Atlas> atlases = new HashSet<>();

    public static Collection<Atlas> getAtlases() {
        return Collections.unmodifiableSet(atlases);
    }

    public final String spritesheet;
    protected final Atlas parent;
    protected int size;
    protected final List<Texture> textures = new CopyOnWriteArrayList<>();
    private Tessellator tessellator;
    protected BufferedImage imageCache;

    public Atlas(final String spritesheet, final int size) {
        this(spritesheet, size, null);
    }

    public Atlas(final String spritesheet, final int size, final Atlas parent) {
        this.spritesheet = spritesheet;
        this.parent = parent;
        if (parent != null) {
            if (parent instanceof ExpandableAtlas)
                throw new UnsupportedOperationException("Parent atlas can't be expandable!");
            else
                this.size = parent.size + size;
        } else
            this.size = size;
        atlases.add(this);
        init();
    }

    protected abstract void init();

    public InputStream getStream() {
        return TextureHelper.getTextureStream(spritesheet);
    }

    public BufferedImage getImage() {
        return imageCache == null ? imageCache = TextureHelper.getTexture(spritesheet) : imageCache;
    }

    public void refreshTextures() {
        imageCache = null;
    }

    final <E extends Atlas> E setTessellator(Tessellator tessellator) {
        if (this.tessellator == null) {
            this.tessellator = tessellator;
            //noinspection unchecked
            return (E) this;
        } else
            throw new UnsupportedOperationException("Tried setting a new tessellator for " + spritesheet + " texture atlas, but there's already a tessellator set up.");
    }

    public final <E extends Atlas> E initTessellator() {
        return setTessellator(TessellatorAccessor.newInst(2097152));
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

    public final Tessellator getTessellator() {
        return tessellator;
    }

    public final int getAtlasTextureID() {
        //noinspection deprecation
        return ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager.getTextureId(spritesheet);
    }

    public final void bindAtlas() {
        //noinspection deprecation
        ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager.bindTexture(getAtlasTextureID());
    }

    public final Texture getTexture(int textureIndex) {
        return applyInherited(textureIndex, textures::get, Atlas::getTexture);
    }

    public final int getUnitSize() {
        return parent == null ? size : size - parent.size;
    }

    public <T extends StationTextureBinder> T addTextureBinder(int staticReferenceTextureIndex, Function<Texture, T> initializer) {
        return addTextureBinder(getTexture(staticReferenceTextureIndex), initializer);
    }

    public <T extends StationTextureBinder> T addTextureBinder(Texture staticReference, Function<Texture, T> initializer) {
        T textureBinder = initializer.apply(staticReference);
        //noinspection deprecation
        ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager.addTextureBinder(textureBinder);
        return textureBinder;
    }

    public AnimatedTextureBinder addAnimationBinder(String animationPath, int animationRate, int staticReferenceTextureIndex) {
        return addTextureBinder(staticReferenceTextureIndex, texture -> new AnimatedTextureBinder(texture, animationPath, animationRate));
    }

    public AnimatedTextureBinder addAnimationBinder(String animationPath, int animationRate, Texture staticReference) {
        return addTextureBinder(staticReference, texture -> new AnimatedTextureBinder(texture, animationPath, animationRate));
    }

    public class Texture {

        public final int index;
        @Getter
        protected int
                x, y,
                width, height;
        @Getter
        protected float
                startU, endU,
                startV, endV;

        protected Texture(int index, int x, int y, int width, int height) {
            this.index = index;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            updateUVs();
        }

        public final Atlas getAtlas() {
            return Atlas.this;
        }

        protected final void updateUVs() {
            BufferedImage image = getAtlas().getImage();
            int
                    atlasWidth = image.getWidth(),
                    atlasHeight = image.getHeight();
            this.startU = (float) x / atlasWidth;
            this.endU = (float) (x + width) / atlasWidth;
            this.startV = (float) y / atlasHeight;
            this.endV = (float) (y + height) / atlasHeight;
        }
    }
}
