package net.modificationstation.stationapi.api.common.packet;

import java.util.*;

public interface StationHandshake {

    String getStationAPI();

    String getVersion();

    Map<String, String> getMods();
}
