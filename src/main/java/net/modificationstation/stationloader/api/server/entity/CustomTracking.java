package net.modificationstation.stationloader.api.server.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public interface CustomTracking {

    int getTrackingDistance();

    int getUpdateFrequency();

    default boolean sendVelocity() {
        return false;
    }
}
