package net.modificationstation.stationapi.api.event.oredict;

import net.mine_diver.unsafeevents.Event;

public class TagRegisterEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
