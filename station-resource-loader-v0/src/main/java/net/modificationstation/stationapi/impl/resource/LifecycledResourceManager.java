package net.modificationstation.stationapi.impl.resource;

import net.modificationstation.stationapi.api.resource.ResourceManager;

public interface LifecycledResourceManager extends ResourceManager, AutoCloseable {
    void close();
}
