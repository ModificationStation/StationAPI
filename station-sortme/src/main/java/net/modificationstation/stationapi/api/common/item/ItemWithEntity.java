package net.modificationstation.stationapi.api.common.item;

import net.minecraft.util.io.CompoundTag;

public interface ItemWithEntity {

    Supplier<ItemEntity> getItemEntityFactory();

    Function<CompoundTag, ItemEntity> getItemEntityNBTFactory();
}
