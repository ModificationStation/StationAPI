package net.modificationstation.stationapi.api.entity;

import net.modificationstation.stationapi.api.block.TeleportationManager;

public interface HasTeleportationManager {

    void setTeleportationManager(TeleportationManager manager);

    TeleportationManager getTeleportationManager();
}
