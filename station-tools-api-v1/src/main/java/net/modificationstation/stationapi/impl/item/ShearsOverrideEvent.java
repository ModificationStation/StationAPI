package net.modificationstation.stationapi.impl.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.item.ItemInstance;

@SuperBuilder
public class ShearsOverrideEvent extends Event {

    public final ItemInstance itemInstance;
    public boolean overrideShears;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}