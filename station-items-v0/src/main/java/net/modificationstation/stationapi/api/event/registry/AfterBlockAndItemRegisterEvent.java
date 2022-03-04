package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.Event;

public class AfterBlockAndItemRegisterEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
