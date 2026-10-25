package net.modificationstation.stationapi.api.network;

import java.util.Map;

public interface ModdedPacketHandler {
    boolean isModded();

    Map<String, String> getMods();
}
