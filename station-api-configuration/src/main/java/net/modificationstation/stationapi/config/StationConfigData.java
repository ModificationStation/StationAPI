package net.modificationstation.stationapi.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class StationConfigData {

    @ConfigEntry(
            name = "gui.config.stationapi.main.loading_screen_option"
    )
    public LoadingScreenOption loadingScreenOption = LoadingScreenOption.SHOW;
}