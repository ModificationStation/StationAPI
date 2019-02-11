package net.modificationstation.stationloader.events.common.mods;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationloader.events.common.Event;

public class SLInitializationEvent extends Event{

    public SLInitializationEvent(Minecraft minecraft) {
        eventData = minecraft;
    }
    public Minecraft getMinecraft() {
        return eventData;
    }
    private Minecraft eventData;
}
