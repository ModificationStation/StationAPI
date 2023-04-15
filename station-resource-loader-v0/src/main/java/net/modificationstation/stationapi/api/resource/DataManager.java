package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.resource.DataResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.resource.ReloadableResourceManager;

public class DataManager extends ReloadableResourceManager {

    public static final DataManager INSTANCE = Util.make(
            new DataManager(),
            resourceManager -> StationAPI.EVENT_BUS.post(
                    DataResourceReloaderRegisterEvent.builder()
                            .resourceManager(resourceManager)
                            .build()
            )
    );

    private DataManager() {
        super(ResourceType.SERVER_DATA);
    }
}
