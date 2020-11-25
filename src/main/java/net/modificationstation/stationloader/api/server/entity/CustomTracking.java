package net.modificationstation.stationloader.api.server.entity;

public interface CustomTracking {

    int getTrackingDistance();

    int getUpdateFrequency();

    default boolean sendVelocity() {
        return false;
    }
}
