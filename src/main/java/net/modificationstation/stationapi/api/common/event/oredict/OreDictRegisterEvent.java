package net.modificationstation.stationapi.api.common.event.oredict;

import net.modificationstation.stationapi.api.common.event.Event;

public class OreDictRegisterEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
