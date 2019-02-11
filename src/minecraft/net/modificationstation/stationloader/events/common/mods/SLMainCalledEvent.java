package net.modificationstation.stationloader.events.common.mods;

import net.modificationstation.stationloader.events.common.Event;

public class SLMainCalledEvent extends Event {
    public SLMainCalledEvent(String args[]) {
        eventData = args;
    }
    public String[] getMainArgs() {
        return eventData;
    }
    private String eventData[];
}
