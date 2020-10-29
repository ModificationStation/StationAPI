package net.modificationstation.stationloader.api.common.packet;

import java.util.Map;

public interface StationHandshake {

    String getStationLoader();
    String getVersion();
    Map<String, String> getMods();
}
