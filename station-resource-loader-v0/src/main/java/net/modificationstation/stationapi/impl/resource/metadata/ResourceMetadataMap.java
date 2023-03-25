package net.modificationstation.stationapi.impl.resource.metadata;

import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;

import java.util.Map;

public class ResourceMetadataMap {
    private static final ResourceMetadataMap EMPTY = new ResourceMetadataMap(Map.of());
    private final Map<ResourceMetadataReader<?>, ?> values;

    private ResourceMetadataMap(Map<ResourceMetadataReader<?>, ?> values) {
        this.values = values;
    }

    public <T> T get(ResourceMetadataReader<T> reader) {
        //noinspection unchecked
        return (T)this.values.get(reader);
    }

    public static ResourceMetadataMap of() {
        return EMPTY;
    }

    public static <T> ResourceMetadataMap of(ResourceMetadataReader<T> reader, T value) {
        return new ResourceMetadataMap(Map.of(reader, value));
    }

    public static <T1, T2> ResourceMetadataMap of(ResourceMetadataReader<T1> reader, T1 value, ResourceMetadataReader<T2> reader2, T2 value2) {
        return new ResourceMetadataMap(Map.of(reader, value, reader2, value2));
    }
}

