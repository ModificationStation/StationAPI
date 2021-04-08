package net.modificationstation.stationapi.api.event.mod;

import net.mine_diver.unsafeevents.Event;

/**
 * Initialization event called for mods to mostly just register event listeners, since the events are already done in {@link PreInitEvent}, or load the config.
 * Some additional setup can be done as well, but Minecraft classes can not be referenced during this event.
 * @author mine_diver
 */
public class InitEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
