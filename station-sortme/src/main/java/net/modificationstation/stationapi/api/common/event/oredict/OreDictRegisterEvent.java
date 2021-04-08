package net.modificationstation.stationapi.api.common.event.oredict;

import net.mine_diver.unsafeevents.Event;

public class OreDictRegisterEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
