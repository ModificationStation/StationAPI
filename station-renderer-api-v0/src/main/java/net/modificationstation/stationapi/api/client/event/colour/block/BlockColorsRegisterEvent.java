package net.modificationstation.stationapi.api.client.event.colour.block;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.colour.block.BlockColors;

@SuperBuilder
public class BlockColorsRegisterEvent extends Event {

    public final BlockColors blockColors;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
