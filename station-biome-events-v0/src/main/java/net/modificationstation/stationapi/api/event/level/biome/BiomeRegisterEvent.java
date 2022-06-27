package net.modificationstation.stationapi.api.event.level.biome;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;

@SuperBuilder
public class BiomeRegisterEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
