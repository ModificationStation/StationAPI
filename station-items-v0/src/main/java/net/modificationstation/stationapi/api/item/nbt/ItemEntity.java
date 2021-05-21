package net.modificationstation.stationapi.api.item.nbt;

import net.minecraft.util.io.CompoundTag;

// TODO: refactor. Use a class instead of interface, make default logic for copy, split, etc. Make stackable if NBT matches.
public interface ItemEntity {

    ItemEntity copy();

    default ItemEntity split(int countToTake) {
        return copy();
    }

    void writeToNBT(CompoundTag tag);
}
