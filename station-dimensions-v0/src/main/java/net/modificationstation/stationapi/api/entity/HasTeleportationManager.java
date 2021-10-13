package net.modificationstation.stationapi.api.entity;

import net.modificationstation.stationapi.api.level.dimension.TeleportationManager;

public interface HasTeleportationManager {

    void setTeleportationManager(TeleportationManager manager);

    TeleportationManager getTeleportationManager();
}
