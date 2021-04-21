package net.modificationstation.stationapi.api.event.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;

public class IsItemEffectiveOnBlockEvent extends ItemInstanceEvent {

    public final BlockBase block;
    public final int meta;
    public boolean effective;

    public IsItemEffectiveOnBlockEvent(ItemInstance itemInstance, BlockBase block, int meta, boolean effective) {
        super(itemInstance);
        this.block = block;
        this.meta = meta;
        this.effective = effective;
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
