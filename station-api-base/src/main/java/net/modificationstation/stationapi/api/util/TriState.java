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

    /**
     * Gets the value of this tri-state.
     * If the value is {@link TriState#UNSET} then use the supplied value.
     *
     * @param value the value to fall back to
     * @return the value of the tri-state or the supplied value if {@link TriState#UNSET}.
     */
    public boolean orElse(boolean value) {
        return this == UNSET ? value : this.getBool();
    }

    @API
    public boolean getBool() {
        Boolean b = getBoolObj();
        if (b == null)
            throw new UnsupportedOperationException("Can't convert " + this + " to boolean!");
        return b;
    }

    private final Boolean value;
}
