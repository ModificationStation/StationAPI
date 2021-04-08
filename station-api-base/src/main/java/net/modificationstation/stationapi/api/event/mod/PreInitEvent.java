package net.modificationstation.stationapi.api.event.mod;

import net.mine_diver.unsafeevents.Event;

/**
 * PreInitialization event called for mods to do some stuff right after the preLaunch and StAPI setup.
 * Some additional setup can be done as well, but Minecraft classes can not be referenced during this event.
 * @author mine_diver
 */
public class PreInitEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
