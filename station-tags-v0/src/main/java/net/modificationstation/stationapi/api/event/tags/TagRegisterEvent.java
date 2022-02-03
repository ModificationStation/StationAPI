package net.modificationstation.stationapi.api.event.tags;

import net.mine_diver.unsafeevents.Event;

public class TagRegisterEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
