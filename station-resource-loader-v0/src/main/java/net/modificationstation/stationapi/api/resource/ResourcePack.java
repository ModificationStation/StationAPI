package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.function.BiConsumer;

public interface ResourcePack extends AutoCloseable {
    String METADATA_PATH_SUFFIX = ".mcmeta";
    String PACK_METADATA_NAME = "pack.mcmeta";

    @Nullable
    InputSupplier<InputStream> openRoot(String... segments);

    @Nullable
    InputSupplier<InputStream> open(ResourceType type, Identifier id);

    void findResources(ResourceType type, String namespace, String prefix, ResultConsumer consumer);

    Set<String> getNamespaces(ResourceType type);

    @Nullable
    <T> T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException;

    String getName();

    default boolean isAlwaysStable() {
        return false;
    }

    void close();

    @FunctionalInterface
    interface ResultConsumer extends BiConsumer<Identifier, InputSupplier<InputStream>> {}
}
