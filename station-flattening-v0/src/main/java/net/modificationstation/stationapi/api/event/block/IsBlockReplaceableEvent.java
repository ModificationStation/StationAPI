package net.modificationstation.stationapi.api.event.block;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;

@SuperBuilder
public class IsBlockReplaceableEvent extends Event {

    public final ItemPlacementContext context;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
