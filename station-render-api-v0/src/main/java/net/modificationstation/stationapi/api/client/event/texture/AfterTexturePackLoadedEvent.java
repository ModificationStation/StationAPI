package net.modificationstation.stationapi.api.client.event.texture;

import net.mine_diver.unsafeevents.Event;

public class AfterTexturePackLoadedEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
