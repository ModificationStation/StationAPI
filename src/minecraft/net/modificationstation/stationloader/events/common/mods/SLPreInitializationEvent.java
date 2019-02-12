package net.modificationstation.stationloader.events.common.mods;

import java.util.logging.Logger;

import net.modificationstation.stationloader.events.common.Event;

public class SLPreInitializationEvent extends Event{
    public SLPreInitializationEvent() {
        specificModEvent = false;
    }
    public SLPreInitializationEvent(String logName) {
        eventData = logName;
    }
    public Logger getModLog() {
        return Logger.getLogger(eventData);
    }
    @Override
    public void process() {
        
    }
    private String eventData;
    private boolean specificModEvent = true;
}
