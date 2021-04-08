package net.modificationstation.stationapi.api.event.level.biome;

import net.mine_diver.unsafeevents.Event;

public class BiomeRegisterEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
