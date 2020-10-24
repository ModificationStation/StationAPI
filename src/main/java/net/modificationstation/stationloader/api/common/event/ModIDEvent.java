package net.modificationstation.stationloader.api.common.event;

import net.fabricmc.loader.api.metadata.ModMetadata;

public interface ModIDEvent<T> extends Event<T> {

    void register(T listener, ModMetadata data);

    String getListenerModID(T listener);
}
