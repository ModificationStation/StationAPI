package net.modificationstation.stationapi.api.client.texture.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.resource.ResourceFinder;
import net.modificationstation.stationapi.api.resource.ResourceManager;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public record DirectoryAtlasSource(String sourcePath, String idPrefix) implements AtlasSource {
    public static final MapCodec<DirectoryAtlasSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("source").forGetter(DirectoryAtlasSource::sourcePath), Codec.STRING.fieldOf("prefix").forGetter(DirectoryAtlasSource::idPrefix)).apply(instance, DirectoryAtlasSource::new));

    @Override
    public void load(ResourceManager resourceManager, AtlasSource.SpriteRegions regions) {
        ResourceFinder resourceFinder = new ResourceFinder(NAMESPACE + "/textures/" + this.sourcePath, ".png");
        resourceFinder.findResources(resourceManager).forEach((identifier, resource) -> regions.add(resourceFinder.toResourceId(identifier).withPrefixedPath(this.idPrefix), resource));
    }

    @Override
    public MapCodec<DirectoryAtlasSource> getCodec() {
        return CODEC;
    }
}

