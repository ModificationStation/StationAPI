package net.modificationstation.stationapi.api.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.server.network.StationServerConnectionListener;
import net.modificationstation.stationapi.api.util.Util;

@Environment(EnvType.SERVER)
public interface StationMinecraftServer {
    default StationServerConnectionListener getStationConnectionListener() {
        return Util.assertImpl();
    }
}
