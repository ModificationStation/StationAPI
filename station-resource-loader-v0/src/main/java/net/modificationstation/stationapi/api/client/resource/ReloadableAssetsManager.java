package net.modificationstation.stationapi.api.client.resource;

import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.impl.resource.ReloadableResourceManager;

public final class ReloadableAssetsManager extends ReloadableResourceManager {

    public static final ReloadableAssetsManager INSTANCE = new ReloadableAssetsManager();

    private ReloadableAssetsManager() {
        super(ResourceType.CLIENT_RESOURCES);
    }
}
