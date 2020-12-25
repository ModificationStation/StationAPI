package net.modificationstation.stationapi.api.common.packet;

import java.util.Map;

public interface StationHandshake {

    String getStationAPI();

    String getVersion();

    Map<String, String> getMods();
}
