package net.modificationstation.stationapi.api.config;

public interface PostConfigLoadedListener {

    /**
     * Mostly useful for config post-processing on edge cases.
     * @see net.modificationstation.stationapi.impl.config.EventStorage.EventSource
     */
    void PostConfigLoaded(int source);
}
