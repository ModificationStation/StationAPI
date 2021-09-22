package net.modificationstation.stationapi.api.client.texture.atlas;

import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.TexturePackDependent;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;
import uk.co.benjiweber.expressions.function.ObjIntFunction;

import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public abstract class Atlas implements TexturePackDependent {

    private static final Set<Atlas> atlases = new HashSet<>();

    public static Collection<Atlas> getAtlases() {
        return Collections.unmodifiableSet(atlases);
    }

    public final String spritesheet;
    protected final Atlas parent;
    protected int size;
    public final boolean fixedSize;
    protected final List<Sprite> textures = new CopyOnWriteArrayList<>();
    private Tessellator tessellator;
    protected BufferedImage imageCache;

    public Atlas(final String spritesheet, final int size, final boolean fixedSize) {
        this(spritesheet, size, fixedSize, null);
    }

    public Atlas(final String spritesheet, final int size, final boolean fixedSize, final Atlas parent) {
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

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        imageCache = null;
    }

    public final <E extends Atlas> E setTessellator(Tessellator tessellator) {
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

    public final Sprite getTexture(int textureIndex) {
        return applyInherited(textureIndex, textures::get, Atlas::getTexture);
    }

    public final int getUnitSize() {
        return parent == null ? size : size - parent.size;
    }

    public <T extends StationTextureBinder> T addTextureBinder(int staticReferenceTextureIndex, Function<Sprite, T> initializer) {
        return addTextureBinder(getTexture(staticReferenceTextureIndex), initializer);
    }

    public <T extends StationTextureBinder> T addTextureBinder(Sprite staticReference, Function<Sprite, T> initializer) {
        T textureBinder = initializer.apply(staticReference);
        //noinspection deprecation
        ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager.addTextureBinder(textureBinder);
        return textureBinder;
    }

    public class Sprite {

        public final int index;
        @Getter
        protected int
                x, y,
                width, height;
        @Getter
        protected double
                startU, endU,
                startV, endV;

        protected Sprite(int index, int x, int y, int width, int height) {
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
            this.startU = (double) x / atlasWidth;
            this.endU = (double) (x + width) / atlasWidth;
            this.startV = (double) y / atlasHeight;
            this.endV = (double) (y + height) / atlasHeight;
        }
    }
}
