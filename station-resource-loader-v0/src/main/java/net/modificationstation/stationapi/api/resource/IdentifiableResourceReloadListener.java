package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.Collection;
import java.util.Collections;

/**
 * Interface for "identifiable" resource reload listeners.
 *
 * <p>"Identifiable" listeners have a unique identifier, which can be depended on,
 * and can provide dependencies that they would like to see executed before
 * themselves.
 */
public interface IdentifiableResourceReloadListener extends ResourceReloader {
    /**
     * @return The unique identifier of this listener.
     */
    Identifier getId();

    /**
     * @return The identifiers of listeners this listener expects to have been
     * executed before itself. Please keep in mind that this only takes effect
     * during the application stage!
     */
    default Collection<Identifier> getDependencies() {
        return Collections.emptyList();
    }
}
