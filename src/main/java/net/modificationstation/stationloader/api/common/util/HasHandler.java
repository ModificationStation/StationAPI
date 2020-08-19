package net.modificationstation.stationloader.api.common.util;

public interface HasHandler<T> {

    default void setHandler(T handler) {
        throw new RuntimeException("You shouldn't be able to access this!");
    }
}
