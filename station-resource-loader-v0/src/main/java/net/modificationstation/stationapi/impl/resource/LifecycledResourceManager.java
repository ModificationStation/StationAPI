package net.modificationstation.stationapi.impl.resource;

import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.resource.ResourceType;

public interface LifecycledResourceManager extends ResourceManager, AutoCloseable {
    ResourceType getResourceType();
    void close();
}
