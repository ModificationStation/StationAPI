package net.modificationstation.stationapi.api.client.texture.atlas;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.client.texture.SpriteContents;
import net.modificationstation.stationapi.api.client.texture.SpriteDimensions;
import net.modificationstation.stationapi.api.util.Identifier;
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

public record PalettedPermutationsAtlasSource(List<Identifier> textures, Identifier paletteKey, Map<String, Identifier> permutations, String separator) implements AtlasSource {
    public static final String DEFAULT_SEPARATOR = "_";
    public static final MapCodec<PalettedPermutationsAtlasSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.list(Identifier.CODEC).fieldOf("textures").forGetter(PalettedPermutationsAtlasSource::textures),
                    Identifier.CODEC.fieldOf("palette_key").forGetter(PalettedPermutationsAtlasSource::paletteKey),
                    Codec.unboundedMap(Codec.STRING, Identifier.CODEC).fieldOf("permutations").forGetter(PalettedPermutationsAtlasSource::permutations),
                    Codec.STRING.optionalFieldOf("separator", DEFAULT_SEPARATOR).forGetter(PalettedPermutationsAtlasSource::separator)
            ).apply(instance, PalettedPermutationsAtlasSource::new)
    );

    @Override
    public void load(ResourceManager resourceManager, AtlasSource.SpriteRegions regions) {
        Supplier<int[]> supplier = Suppliers.memoize(() -> PalettedPermutationsAtlasSource.open(resourceManager, this.paletteKey));
        Map<String, Supplier<IntUnaryOperator>> map = new HashMap<>();
        this.permutations.forEach((string, identifier) -> map.put(string, Suppliers.memoize(() -> PalettedPermutationsAtlasSource.toMapper(supplier.get(), PalettedPermutationsAtlasSource.open(resourceManager, identifier)))));
        for (Identifier identifier2 : this.textures) {
            Identifier identifier22 = RESOURCE_FINDER.toResourcePath(identifier2);
            Optional<Resource> optional = resourceManager.getResource(identifier22);
            if (optional.isEmpty()) {
                LOGGER.warn("Unable to find texture {}", identifier22);
                continue;
            }
            Sprite sprite = new Sprite(identifier22, optional.get(), map.size());
            for (Map.Entry<String, Supplier<IntUnaryOperator>> entry : map.entrySet()) {
                Identifier identifier3 = identifier2.withSuffixedPath(this.separator + entry.getKey());
                regions.add(identifier3, new PalettedSpriteRegion(sprite, entry.getValue(), identifier3));
            }
        }
    }

    private static IntUnaryOperator toMapper(int[] from, int[] to) {
        if (to.length != from.length) {
            LOGGER.warn("Palette mapping has different sizes: {} and {}", from.length, to.length);
            throw new IllegalArgumentException();
        }
        Int2IntOpenHashMap int2IntMap = new Int2IntOpenHashMap(to.length);
        for (int i = 0; i < from.length; ++i) {
            int c = from[i];
            if (ColorHelper.Abgr.getAlpha(c) == 0) continue;
            int2IntMap.put(ColorHelper.Abgr.getBgr(c), to[i]);
        }
        return i -> {
            int c = ColorHelper.Abgr.getAlpha(i);
            if (c == 0) return i;
            int k = ColorHelper.Abgr.getBgr(i);
            int l = int2IntMap.getOrDefault(k, ColorHelper.Abgr.toOpaque(k));
            int m = ColorHelper.Abgr.getAlpha(l);
            return ColorHelper.Abgr.withAlpha(c * m / 255, l);
        };
    }

    public static int[] open(ResourceManager resourceManager, Identifier texture) {
        Optional<Resource> optional = resourceManager.getResource(RESOURCE_FINDER.toResourcePath(texture));
        if (optional.isEmpty()) {
            LOGGER.error("Failed to load palette image {}", texture);
            throw new IllegalArgumentException();
        }
        try (
                InputStream inputStream = optional.get().getInputStream();
                NativeImage nativeImage = NativeImage.read(inputStream)
        ) {
            return nativeImage.makePixelArray();
        } catch (Exception exception) {
            LOGGER.error("Couldn't load texture {}", texture, exception);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public MapCodec<PalettedPermutationsAtlasSource> getCodec() {
        return CODEC;
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

