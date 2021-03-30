package net.modificationstation.stationapi.api.common.event.level.biome;

import net.modificationstation.stationapi.api.common.event.Event;

public class BiomeRegisterEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
