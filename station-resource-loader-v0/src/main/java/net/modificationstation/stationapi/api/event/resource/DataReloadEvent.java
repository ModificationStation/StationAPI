package net.modificationstation.stationapi.api.event.resource;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;

@SuperBuilder
public class DataReloadEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
