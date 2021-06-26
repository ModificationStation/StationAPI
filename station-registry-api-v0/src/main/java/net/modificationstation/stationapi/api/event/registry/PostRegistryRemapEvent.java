package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.Event;

/**
 * An event that's fired after all {@link net.modificationstation.stationapi.api.registry.LevelSerialRegistry}
 * instances have been remapped.
 *
 * @author mine_diver
 * @see net.modificationstation.stationapi.api.registry.LevelSerialRegistry
 */
public class PostRegistryRemapEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
