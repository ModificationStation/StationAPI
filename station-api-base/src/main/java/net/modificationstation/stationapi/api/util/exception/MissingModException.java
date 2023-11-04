package net.modificationstation.stationapi.api.util.exception;

public class MissingModException extends RuntimeException {
    public MissingModException(String namespace) {
        super("Namespace " + namespace + " isn't present in the runtime!");
    }
}
