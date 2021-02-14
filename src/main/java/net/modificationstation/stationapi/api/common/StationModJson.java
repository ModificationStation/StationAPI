package net.modificationstation.stationapi.api.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StationModJson {

    /**
     * Libraries to be downloaded and loaded by StationAPI.
     * URL is for the repo and name is the gradle-style name for the library.
     */
    private final List<Library> libraries = new ArrayList<>();

    private final List<?> entrypoints = new ArrayList<>();

    /**
     * Make sure client has the mod loaded if the mod is installed on server.
     */
    private final boolean enforceClientLoaded = true;
}
