package net.modificationstation.stationapi.api.client.resource;

import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.AssetsResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.resource.ReloadableResourceManagerImpl;

public final class ReloadableAssetsManager extends ReloadableResourceManagerImpl {

    public static final ReloadableAssetsManager INSTANCE = Util.make(new ReloadableAssetsManager(), resourceManager -> StationAPI.EVENT_BUS.post(AssetsResourceReloaderRegisterEvent.builder().resourceManager(resourceManager).build()));

    private ReloadableAssetsManager() {
        super(ResourceType.CLIENT_RESOURCES);
    }
}
