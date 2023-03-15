package net.modificationstation.stationapi.api.client.texture.atlas;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.client.texture.SpriteContents;
import net.modificationstation.stationapi.api.client.texture.SpriteDimensions;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntUnaryOperator;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class PalettedPermutationsAtlasSource implements AtlasSource {
    public static final Codec<PalettedPermutationsAtlasSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.list(Identifier.CODEC).fieldOf("textures").forGetter(palettedPermutationsAtlasSource -> palettedPermutationsAtlasSource.textures), Identifier.CODEC.fieldOf("palette_key").forGetter(palettedPermutationsAtlasSource -> palettedPermutationsAtlasSource.paletteKey), Codec.unboundedMap(Codec.STRING, Identifier.CODEC).fieldOf("permutations").forGetter(palettedPermutationsAtlasSource -> palettedPermutationsAtlasSource.permutations)).apply(instance, PalettedPermutationsAtlasSource::new));
    private final List<Identifier> textures;
    private final Map<String, Identifier> permutations;
    private final Identifier paletteKey;

    private PalettedPermutationsAtlasSource(List<Identifier> textures, Identifier paletteKey, Map<String, Identifier> permutations) {
        this.textures = textures;
        this.permutations = permutations;
        this.paletteKey = paletteKey;
    }

    @Override
    public void load(ResourceManager resourceManager, AtlasSource.SpriteRegions regions) {
        Supplier<int[]> supplier = Suppliers.memoize(() -> PalettedPermutationsAtlasSource.method_48486(resourceManager, this.paletteKey));
        Map<String, Supplier<IntUnaryOperator>> map = new HashMap<>();
        this.permutations.forEach((string, identifier) -> map.put(string, Suppliers.memoize(() -> PalettedPermutationsAtlasSource.method_48492(supplier.get(), PalettedPermutationsAtlasSource.method_48486(resourceManager, identifier)))));
        for (Identifier identifier2 : this.textures) {
            Identifier identifier22 = RESOURCE_FINDER.toResourcePath(identifier2);
            Optional<Resource> optional = resourceManager.getResource(identifier22);
            if (optional.isEmpty()) {
                LOGGER.warn("Unable to find texture {}", identifier22);
                continue;
            }
            Sprite sprite = new Sprite(identifier22, optional.get(), map.size());
            for (Map.Entry<String, Supplier<IntUnaryOperator>> entry : map.entrySet()) {
                Identifier identifier3 = identifier2.append("_" + entry.getKey());
                regions.add(identifier3, new PalettedSpriteRegion(sprite, entry.getValue(), identifier3));
            }
        }
    }

    private static IntUnaryOperator method_48492(int[] is, int[] js) {
        if (js.length != is.length) {
            LOGGER.warn("Palette mapping has different sizes: {} and {}", is.length, js.length);
            throw new IllegalArgumentException();
        }
        Int2IntOpenHashMap int2IntMap = new Int2IntOpenHashMap(js.length);
        for (int i2 = 0; i2 < is.length; ++i2) {
            int j = is[i2];
            if (ColorHelper.Abgr.getAlpha(j) == 0) continue;
            int2IntMap.put(ColorHelper.Abgr.getBgr(j), js[i2]);
        }
        return i -> {
            int j = ColorHelper.Abgr.getAlpha(i);
            if (j == 0) return i;
            int k = ColorHelper.Abgr.getBgr(i);
            int l = int2IntMap.getOrDefault(k, ColorHelper.Abgr.toOpaque(k));
            int m = ColorHelper.Abgr.getAlpha(l);
            return ColorHelper.Abgr.withAlpha(j * m / 255, l);
        };
    }

    public static int[] method_48486(ResourceManager resourceManager, Identifier identifier) {
        Optional<Resource> optional = resourceManager.getResource(RESOURCE_FINDER.toResourcePath(identifier));
        if (optional.isEmpty()) {
            LOGGER.error("Failed to load palette image {}", identifier);
            throw new IllegalArgumentException();
        }
        try (
                InputStream inputStream = optional.get().getInputStream();
                NativeImage nativeImage = NativeImage.read(inputStream)
        ) {
            return nativeImage.makePixelArray();
        } catch (Exception exception) {
            LOGGER.error("Couldn't load texture {}", identifier, exception);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public AtlasSourceType getType() {
        return AtlasSourceManager.PALETTED_PERMUTATIONS;
    }

    record PalettedSpriteRegion(Sprite baseImage, java.util.function.Supplier<IntUnaryOperator> palette, Identifier permutationLocation) implements AtlasSource.SpriteRegion {
        @Override
        @Nullable
        public SpriteContents get() {
            try {
                NativeImage nativeImage = this.baseImage.read().apply(this.palette.get());
                return new SpriteContents(this.permutationLocation, new SpriteDimensions(nativeImage.getWidth(), nativeImage.getHeight()), nativeImage, AnimationResourceMetadata.EMPTY);
            } catch (IOException | IllegalArgumentException exception) {
                LOGGER.error("unable to apply palette to {}", this.permutationLocation, exception);
                return null;
            } finally {
                this.baseImage.close();
            }
        }

        @Override
        public void close() {
            this.baseImage.close();
        }
    }
}

