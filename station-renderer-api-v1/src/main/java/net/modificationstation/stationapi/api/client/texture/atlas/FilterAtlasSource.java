package net.modificationstation.stationapi.api.client.texture.atlas;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.resource.metadata.BlockEntry;

public record FilterAtlasSource(BlockEntry pattern) implements AtlasSource {
    public static final MapCodec<FilterAtlasSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BlockEntry.CODEC.fieldOf("pattern").forGetter(FilterAtlasSource::pattern)).apply(instance, FilterAtlasSource::new));

    @Override
    public void load(ResourceManager resourceManager, AtlasSource.SpriteRegions regions) {
        regions.removeIf(this.pattern.getIdentifierPredicate());
    }

    @Override
    public MapCodec<FilterAtlasSource> getCodec() {
        return CODEC;
    }
}

