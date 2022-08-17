package net.modificationstation.stationapi.api.event.item;

import lombok.experimental.SuperBuilder;
import net.modificationstation.stationapi.api.block.BlockState;

@SuperBuilder
public final class ItemMiningSpeedMultiplierOnStateEvent extends ItemStackEvent {

    public final BlockState state;
    public float miningSpeedMultiplier;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
