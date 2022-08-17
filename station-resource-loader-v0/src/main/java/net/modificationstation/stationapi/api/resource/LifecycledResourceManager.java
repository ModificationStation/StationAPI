package net.modificationstation.stationapi.api.resource;

/**
 * A lifecycled resource manager is available until it is {@linkplain #close()
 * closed}. In principle, it should not be accessed any more after closing;
 * use another resource manager instead.
 */
public interface LifecycledResourceManager extends ResourceManager, AutoCloseable {
    @Override
    void close();
}

