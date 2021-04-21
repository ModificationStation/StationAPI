package net.modificationstation.stationapi.api.event.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;

public class ItemStrengthOnBlockEvent extends ItemInstanceEvent {

    public final BlockBase block;
    public final int meta;
    public float strength;

    public ItemStrengthOnBlockEvent(ItemInstance itemInstance, BlockBase block, int meta, float strength) {
        super(itemInstance);
        this.block = block;
        this.meta = meta;
        this.strength = strength;
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
