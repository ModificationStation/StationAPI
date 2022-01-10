package net.modificationstation.stationapi.api.item.nbt;

import net.minecraft.util.io.CompoundTag;

@Deprecated
public interface ItemEntity {

    @Deprecated
    ItemEntity copy();

    @Deprecated
    default ItemEntity split(int countToTake) {
        return copy();
    }

    @Deprecated
    void writeToNBT(CompoundTag tag);
}
