package net.modificationstation.stationapi.api.common.event.recipe;

import net.modificationstation.stationapi.api.common.event.Event;

public class BeforeRecipeStatsEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
