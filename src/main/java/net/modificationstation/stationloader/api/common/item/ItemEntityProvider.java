package net.modificationstation.stationloader.api.common.item;

import net.minecraft.util.io.CompoundTag;

public interface ItemEntityProvider {

    ItemEntity newItemEntity();

    ItemEntity readFromNBT(CompoundTag tag);

    void writeToNBT(CompoundTag tag, ItemEntity itemEntity);
}
