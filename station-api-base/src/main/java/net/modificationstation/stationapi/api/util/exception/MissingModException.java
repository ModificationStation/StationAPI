package net.modificationstation.stationapi.api.util.exception;

public class MissingModException extends RuntimeException {
    public MissingModException(String modID) {
        super("ModID " + modID + " isn't present in the runtime!");
    }
}
