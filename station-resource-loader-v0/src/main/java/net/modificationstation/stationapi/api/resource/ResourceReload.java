package net.modificationstation.stationapi.api.resource;

public interface ResourceReload {
    /**
     * Returns a fraction between 0 and 1 indicating the progress of this
     * reload.
     */
    float getProgress();

    boolean isComplete();

    void throwException();
}
