package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A resource pack, providing resources to resource managers.
 * 
 * <p>They are single-use in the lifecycle of a {@linkplain LifecycledResourceManagerImpl
 * lifecycled resource manager}. A {@link ResourcePackProfile} is a persistent
 * representation of the resource packs, and can be used to recreate the packs
 * on demand.
 */
public interface ResourcePack extends AutoCloseable {
    String METADATA_PATH_SUFFIX = ".mcmeta";
    String PACK_METADATA_NAME = "pack.mcmeta";

    @Nullable InputStream openRoot(String var1) throws IOException;

    InputStream open(ResourceType var1, Identifier var2) throws IOException;

    Collection<Identifier> findResources(ResourceType var1, String var2, String var3, Predicate<Identifier> var4);

    boolean contains(ResourceType var1, Identifier var2);

    Set<String> getNamespaces(ResourceType var1);

    @Nullable <T> T parseMetadata(ResourceMetadataReader<T> var1) throws IOException;

    String getName();

    @Override
    void close();
}

