package net.modificationstation.stationapi.api.util;

public enum TriState {

    @API
    TRUE(true),
    @API
    FALSE(false),
    @API
    UNSET(null);

    TriState(Boolean value) {
        this.value = value;
    }

    @API
    public static TriState fromBool(boolean b) {
        return b ? TRUE : FALSE;
    }

    @API
    public static TriState fromBoolObj(Boolean b) {
        return b == null ? UNSET : fromBool(b);
    }

    @API
    public Boolean getBoolObj() {
        return value;
    }

    @API
    public boolean getBool() {
        Boolean b = getBoolObj();
        if (b == null)
            throw new UnsupportedOperationException("Can't convert " + UNSET + " to boolean!");
        return b;
    }

    private final Boolean value;
}
