package net.modificationstation.stationapi.api.client.texture.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.resource.metadata.BlockEntry;

public class FilterAtlasSource implements AtlasSource {
    public static final Codec<FilterAtlasSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(BlockEntry.CODEC.fieldOf("pattern").forGetter(filterAtlasSource -> filterAtlasSource.pattern)).apply(instance, FilterAtlasSource::new));
    private final BlockEntry pattern;

    public FilterAtlasSource(BlockEntry pattern) {
        this.pattern = pattern;
    }

    @Override
    public void load(ResourceManager resourceManager, AtlasSource.SpriteRegions regions) {
        regions.removeIf(this.pattern.getIdentifierPredicate());
    }

    @Override
    public AtlasSourceType getType() {
        return AtlasSourceManager.FILTER;
    }
}

