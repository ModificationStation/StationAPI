package net.modificationstation.stationloader.api.common.item;

import net.minecraft.util.io.CompoundTag;

public interface ItemEntity {

    ItemEntity copy();

    default ItemEntity split(int countToTake) {
        return copy();
    };

    void writeToNBT(CompoundTag tag);
}
