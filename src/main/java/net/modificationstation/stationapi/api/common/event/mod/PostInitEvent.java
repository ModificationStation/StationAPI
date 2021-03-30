package net.modificationstation.stationapi.api.common.event.mod;

import net.modificationstation.stationapi.api.common.event.Event;

/**
 * PostInitialization event called for mods to do their setup when the other mods finished initializing.
 * Minecraft classes can not be referenced during this event.
 * @author mine_diver
 */
public class PostInitEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
