package net.modificationstation.stationapi.api.registry;

public class RemapException extends Exception {
    public RemapException(String message) {
        super(message);
    }

    public RemapException(String message, Throwable t) {
        super(message, t);
    }
}
