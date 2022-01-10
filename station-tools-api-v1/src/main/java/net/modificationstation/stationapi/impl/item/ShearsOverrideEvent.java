package net.modificationstation.stationapi.impl.item;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.item.ItemInstance;

@RequiredArgsConstructor
public class ShearsOverrideEvent extends Event {

    public boolean overrideShears = false;
    public final ItemInstance itemInstance;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}