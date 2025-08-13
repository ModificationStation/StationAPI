package net.modificationstation.stationapi.api.client.texture.atlas;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;

import java.util.Optional;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public record SingleAtlasSource(Identifier resourceId, Optional<Identifier> spriteId) implements AtlasSource {
    public static final MapCodec<SingleAtlasSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Identifier.CODEC.fieldOf("resource").forGetter(singleAtlasSource -> singleAtlasSource.resourceId), Identifier.CODEC.optionalFieldOf("sprite").forGetter(singleAtlasSource -> singleAtlasSource.spriteId)).apply(instance, SingleAtlasSource::new));

    @Override
    public void load(ResourceManager resourceManager, AtlasSource.SpriteRegions regions) {
        Identifier identifier = RESOURCE_FINDER.toResourcePath(this.resourceId);
        Optional<Resource> optional = resourceManager.getResource(identifier);
        if (optional.isPresent()) regions.add(this.spriteId.orElse(this.resourceId), optional.get());
        else LOGGER.warn("Missing sprite: {}", identifier);
    }

    @Override
    public MapCodec<SingleAtlasSource> getCodec() {
        return CODEC;
    }
}

