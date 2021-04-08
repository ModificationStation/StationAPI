package net.modificationstation.stationapi.api.event.recipe;

import net.mine_diver.unsafeevents.Event;

public class BeforeRecipeStatsEvent extends Event {

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
