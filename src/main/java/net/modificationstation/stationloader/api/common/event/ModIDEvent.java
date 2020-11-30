package net.modificationstation.stationloader.api.common.event;

import net.fabricmc.loader.api.ModContainer;

public interface ModIDEvent<T> extends Event<T> {

    @Deprecated
    @Override
    void register(T listener);

    void register(T listener, ModContainer container);

    T getCurrentListener();

    void setCurrentListener(T listener);

    ModContainer getListenerContainer(T listener);
}
