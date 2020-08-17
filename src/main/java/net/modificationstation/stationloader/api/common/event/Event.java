package net.modificationstation.stationloader.api.common.event;

/**
 * Event class
 * 
 * @author mine_diver
 *
 * @param <T>
 **/

public interface Event<T> {

    T getInvoker();

    void register(T listener);
}
