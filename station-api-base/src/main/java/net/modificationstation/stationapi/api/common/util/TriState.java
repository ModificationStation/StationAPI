package net.modificationstation.stationapi.api.common.util;

public enum TriState {

    TRUE(true),
    FALSE(false),
    UNSET(null);

    TriState(Boolean value) {
        this.value = value;
    }

    public static TriState fromBool(boolean b) {
        return b ? TRUE : FALSE;
    }

    public static TriState fromBoolObj(Boolean b) {
        return b == null ? UNSET : fromBool(b);
    }

    public Boolean getBoolObj() {
        return value;
    }

    public boolean getBool() {
        Boolean b = getBoolObj();
        if (b == null)
            throw new UnsupportedOperationException("Can't convert " + UNSET + " to boolean!");
        return b;
    }

    private final Boolean value;
}
