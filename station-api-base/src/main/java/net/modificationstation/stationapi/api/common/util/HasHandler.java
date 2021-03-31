package net.modificationstation.stationapi.api.common.util;

public interface HasHandler<T> {

    default void setHandler(T handler) {
        throw new RuntimeException("You shouldn't be able to access this!");
    }

    default void checkAccess(T handler) {
        if (handler == null)
            throw new RuntimeException("Accessed StationAPI too early!");
    }
}
