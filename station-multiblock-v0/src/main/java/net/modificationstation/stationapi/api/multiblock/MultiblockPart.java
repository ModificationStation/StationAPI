package net.modificationstation.stationapi.api.multiblock;

import net.minecraft.item.ItemInstance;

public class MultiblockPart {

    private final ItemInstance block;
    private final boolean isCenter;

    public MultiblockPart(ItemInstance block, boolean isCenter) {
        this.block = block;
        this.isCenter = isCenter;
    }
}
