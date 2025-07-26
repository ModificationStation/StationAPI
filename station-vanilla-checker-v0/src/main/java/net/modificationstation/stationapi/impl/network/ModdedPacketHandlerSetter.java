package net.modificationstation.stationapi.impl.network;

import java.util.Map;

public interface ModdedPacketHandlerSetter {
    void setModded(boolean value);
    void setModded(Map<String, String> mods);
}
