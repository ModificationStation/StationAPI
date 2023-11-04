package net.modificationstation.stationapi.api.client.texture.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;

import java.util.Optional;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class SingleAtlasSource implements AtlasSource {
    public static final Codec<SingleAtlasSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(Identifier.CODEC.fieldOf("resource").forGetter(singleAtlasSource -> singleAtlasSource.resource), Identifier.CODEC.optionalFieldOf("sprite").forGetter(singleAtlasSource -> singleAtlasSource.sprite)).apply(instance, SingleAtlasSource::new));
    private final Identifier resource;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<Identifier> sprite;

    public SingleAtlasSource(Identifier resource, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Identifier> sprite) {
        this.resource = resource;
        this.sprite = sprite;
    }

    @Override
    public void load(ResourceManager resourceManager, AtlasSource.SpriteRegions regions) {
        Identifier identifier = RESOURCE_FINDER.toResourcePath(this.resource);
        Optional<Resource> optional = resourceManager.getResource(identifier);
        if (optional.isPresent()) regions.add(this.sprite.orElse(this.resource), optional.get());
        else LOGGER.warn("Missing sprite: {}", identifier);
    }

    @Override
    public AtlasSourceType getType() {
        return AtlasSourceManager.SINGLE;
    }
}

