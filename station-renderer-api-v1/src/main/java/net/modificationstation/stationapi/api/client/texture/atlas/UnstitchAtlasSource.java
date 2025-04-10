package net.modificationstation.stationapi.api.client.texture.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.client.texture.MissingSprite;
import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.client.texture.SpriteContents;
import net.modificationstation.stationapi.api.client.texture.SpriteDimensions;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;

import java.util.List;
import java.util.Optional;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public record UnstitchAtlasSource(Identifier resource, List<Region> regions, double divisorX, double divisorY) implements AtlasSource {
    public static final MapCodec<UnstitchAtlasSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("resource").forGetter(UnstitchAtlasSource::resource),
            Codecs.nonEmptyList(Region.CODEC.listOf()).fieldOf("regions").forGetter(UnstitchAtlasSource::regions),
            Codec.DOUBLE.optionalFieldOf("divisor_x", 1.0).forGetter(UnstitchAtlasSource::divisorX),
            Codec.DOUBLE.optionalFieldOf("divisor_y", 1.0).forGetter(UnstitchAtlasSource::divisorY)
    ).apply(instance, UnstitchAtlasSource::new));

    @Override
    public void load(ResourceManager resourceManager, AtlasSource.SpriteRegions regions) {
        Identifier identifier = RESOURCE_FINDER.toResourcePath(this.resource);
        Optional<Resource> optional = resourceManager.getResource(identifier);
        if (optional.isPresent()) {
            Sprite sprite = new Sprite(identifier, optional.get(), this.regions.size());
            for (Region region : this.regions)
                regions.add(region.sprite, new net.modificationstation.stationapi.api.client.texture.atlas.UnstitchAtlasSource.SpriteRegion(sprite, region, this.divisorX, this.divisorY));
        } else LOGGER.warn("Missing sprite: {}", identifier);
    }

    @Override
    public MapCodec<UnstitchAtlasSource> getCodec() {
        return CODEC;
    }

    public record Region(Identifier sprite, double x, double y, double width, double height) {
        public static final Codec<Region> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("sprite").forGetter(Region::sprite),
                Codec.DOUBLE.fieldOf("x").forGetter(Region::x),
                Codec.DOUBLE.fieldOf("y").forGetter(Region::y),
                Codec.DOUBLE.fieldOf("width").forGetter(Region::width),
                Codec.DOUBLE.fieldOf("height").forGetter(Region::height)
        ).apply(instance, Region::new));
    }

    static class SpriteRegion implements AtlasSource.SpriteRegion {
        private final Sprite sprite;
        private final Region region;
        private final double divisorX;
        private final double divisorY;

        SpriteRegion(Sprite sprite, Region region, double divisorX, double divisorY) {
            this.sprite = sprite;
            this.region = region;
            this.divisorX = divisorX;
            this.divisorY = divisorY;
        }

        @Override
        public SpriteContents get() {
            try {
                //noinspection resourceId
                NativeImage nativeImage = this.sprite.read();
                double d = (double)nativeImage.getWidth() / this.divisorX;
                double e = (double)nativeImage.getHeight() / this.divisorY;
                int i = MathHelper.floor(this.region.x * d);
                int j = MathHelper.floor(this.region.y * e);
                int k = MathHelper.floor(this.region.width * d);
                int l = MathHelper.floor(this.region.height * e);
                NativeImage nativeImage2 = new NativeImage(NativeImage.Format.RGBA, k, l, false);
                nativeImage.copyRect(nativeImage2, i, j, 0, 0, k, l, false, false);
                return new SpriteContents(this.region.sprite, new SpriteDimensions(k, l), nativeImage2, AnimationResourceMetadata.EMPTY);
            } catch (Exception exception) {
                LOGGER.error("Failed to unstitch region {}", this.region.sprite, exception);
            } finally {
                this.sprite.close();
            }
            return MissingSprite.createSpriteContents();
        }

        @Override
        public void close() {
            this.sprite.close();
        }
    }
}

