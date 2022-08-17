package net.modificationstation.stationapi.api.client.event.resource;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.resource.ReloadableResourceManager;

@SuperBuilder
public class ClientResourceReloaderRegisterEvent extends Event {

    public final ReloadableResourceManager resourceManager;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
