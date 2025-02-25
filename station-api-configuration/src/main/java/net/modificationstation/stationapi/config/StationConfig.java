package net.modificationstation.stationapi.config;

import net.glasslauncher.mods.gcapi3.api.ConfigRoot;

public class StationConfig {

    @ConfigRoot(
            value = "config",
            visibleName = "StationAPI Configuration",
            nameKey = "gui.config.stationapi.main"
    )
    public static StationConfigData stationConfigData = new StationConfigData();
}
