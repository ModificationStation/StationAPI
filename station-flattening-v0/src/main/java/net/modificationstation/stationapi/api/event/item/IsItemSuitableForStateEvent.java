package net.modificationstation.stationapi.api.event.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockState;

@SuperBuilder
public final class IsItemSuitableForStateEvent extends Event {

    public final ItemInstance itemStack;
    public final BlockState state;
    public boolean suitable;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
