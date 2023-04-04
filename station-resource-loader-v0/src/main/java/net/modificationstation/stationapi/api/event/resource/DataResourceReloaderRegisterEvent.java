package net.modificationstation.stationapi.api.event.resource;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.resource.DataManager;

@SuperBuilder
public class DataResourceReloaderRegisterEvent extends Event {

    public final DataManager resourceManager;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
