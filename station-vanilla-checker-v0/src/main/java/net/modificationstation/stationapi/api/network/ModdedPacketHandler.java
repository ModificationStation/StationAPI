package net.modificationstation.stationapi.api.network;

import java.util.*;

public interface ModdedPacketHandler {

    boolean isModded();

    Map<String, String> getMods();
}
