package net.modificationstation.stationapi.impl.item;

import lombok.experimental.SuperBuilder;
import net.modificationstation.stationapi.api.event.item.ItemStackEvent;

@SuperBuilder
public class ShearsOverrideEvent extends ItemStackEvent {

    public boolean overrideShears;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}