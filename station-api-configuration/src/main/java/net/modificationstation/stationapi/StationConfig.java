package net.modificationstation.stationapi;

import net.modificationstation.stationapi.api.config.ConfigRoot;

public class StationConfig {

    @ConfigRoot(
            value = "config",
            visibleName = "gui.config.stationapi.main"
    )
    public static StationConfigData stationConfigData = new StationConfigData();
}
