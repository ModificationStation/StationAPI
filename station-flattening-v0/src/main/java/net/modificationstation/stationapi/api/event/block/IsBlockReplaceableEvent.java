package net.modificationstation.stationapi.api.event.block;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;

@SuperBuilder
public class IsBlockReplaceableEvent extends Event {
    public final ItemPlacementContext context;
}
