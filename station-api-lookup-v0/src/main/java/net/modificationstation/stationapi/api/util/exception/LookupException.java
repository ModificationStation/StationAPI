package net.modificationstation.stationapi.api.util.exception;

public class LookupException extends RuntimeException {
    public final Class<?> expected;
    public final Class<?> actual;

    public LookupException(Class<?> expected, Class<?> actual) {
        super("Expected " + expected + " instead of " + actual);
        this.expected = expected;
        this.actual = actual;
    }
}
