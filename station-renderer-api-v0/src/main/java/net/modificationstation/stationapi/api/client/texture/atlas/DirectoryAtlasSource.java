package net.modificationstation.stationapi.api.client.texture.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.resource.ResourceFinder;
import net.modificationstation.stationapi.api.resource.ResourceManager;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class DirectoryAtlasSource implements AtlasSource {
    public static final Codec<DirectoryAtlasSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("source").forGetter(directoryAtlasSource -> directoryAtlasSource.source), Codec.STRING.fieldOf("prefix").forGetter(directoryAtlasSource -> directoryAtlasSource.prefix)).apply(instance, DirectoryAtlasSource::new));
    private final String source;
    private final String prefix;

    public DirectoryAtlasSource(String source, String prefix) {
        this.source = source;
        this.prefix = prefix;
    }

    @Override
    public void load(ResourceManager resourceManager, AtlasSource.SpriteRegions regions) {
        ResourceFinder resourceFinder = new ResourceFinder(NAMESPACE + "/textures/" + this.source, ".png");
        resourceFinder.findResources(resourceManager).forEach((identifier, resource) -> regions.add(resourceFinder.toResourceId(identifier).withPrefixedPath(this.prefix), resource));
    }

    @Override
    public AtlasSourceType getType() {
        return AtlasSourceManager.DIRECTORY;
    }
}

