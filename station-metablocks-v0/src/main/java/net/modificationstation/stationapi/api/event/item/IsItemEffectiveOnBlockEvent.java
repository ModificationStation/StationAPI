package net.modificationstation.stationapi.api.event.item;

import lombok.experimental.SuperBuilder;
import net.minecraft.block.BlockBase;

@SuperBuilder
public class IsItemEffectiveOnBlockEvent extends ItemInstanceEvent {

    public final BlockBase block;
    public final int meta;
    public boolean effective;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
