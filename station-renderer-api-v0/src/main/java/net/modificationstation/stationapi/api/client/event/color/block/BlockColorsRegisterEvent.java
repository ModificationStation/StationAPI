package net.modificationstation.stationapi.api.client.event.color.block;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;

@SuperBuilder
public class BlockColorsRegisterEvent extends Event {

    public final BlockColors blockColors;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
