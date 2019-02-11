package net.modificationstation.stationloader.events.common.mods;

import java.util.logging.Logger;

import net.modificationstation.stationloader.events.common.Event;

public class SLPreInitializationEvent extends Event{
    public Logger getModLog() {
        return Logger.getLogger("Mod");
    }
}
