package net.modificationstation.stationapi.api.common.event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ListenerPriority {

    HIGHEST(Integer.MAX_VALUE),
    HIGH(Integer.MAX_VALUE / 2),
    NORMAL(0),
    LOW(Integer.MIN_VALUE / 2),
    LOWEST(Integer.MIN_VALUE),
    CUSTOM;

    public final int numPriority;
    public final boolean custom;

    ListenerPriority() {
        this(0, true);
    }

    ListenerPriority(int numPriority) {
        this(numPriority, false);
    }
}
