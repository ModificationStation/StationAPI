package net.modificationstation.stationapi.api.network;

import net.modificationstation.stationapi.api.util.Util;

public interface StationMinecraftServer {
    default StationServerConnectionListener getStationConnectionListener() {
        return Util.assertImpl();
    }
}
