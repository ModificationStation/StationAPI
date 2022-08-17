package net.modificationstation.stationapi.api.event.item;

import lombok.experimental.SuperBuilder;
import net.modificationstation.stationapi.api.block.BlockState;

@SuperBuilder
public final class IsItemSuitableForStateEvent extends ItemStackEvent {

    public final BlockState state;
    public boolean suitable;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
