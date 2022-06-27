package net.modificationstation.stationapi.api.client.event.colour.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.colour.block.BlockColors;
import net.modificationstation.stationapi.api.client.colour.item.ItemColors;

@SuperBuilder
public class ItemColorsRegisterEvent extends Event {

    public final BlockColors blockColors;
    public final ItemColors itemColors;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
