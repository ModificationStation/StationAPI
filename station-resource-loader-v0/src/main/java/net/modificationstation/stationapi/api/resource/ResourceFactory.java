package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.registry.Identifier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Provides resource access.
 */
@FunctionalInterface
public interface ResourceFactory {
    /**
     * Finds and returns the corresponding resource for a resource's identifier.
     * 
     * <p>Starts by scanning each resource pack from highest priority to lowest. If no resource packs were found
     * to contain the requested entry, will return {@link Optional#empty()}.
     * 
     * <p>The returned resource must be closed to avoid resource leaks.
     * 
     * @param id the resource identifier to search for
     */
    Optional<Resource> getResource(Identifier id);

    default Resource getResourceOrThrow(Identifier identifier) throws FileNotFoundException {
        return this.getResource(identifier).orElseThrow(() -> new FileNotFoundException(identifier.toString()));
    }

    default InputStream open(Identifier identifier) throws IOException {
        return this.getResourceOrThrow(identifier).getInputStream();
    }

    default BufferedReader openAsReader(Identifier identifier) throws IOException {
        return this.getResourceOrThrow(identifier).getReader();
    }
}

