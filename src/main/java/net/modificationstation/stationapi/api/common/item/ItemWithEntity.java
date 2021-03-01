package net.modificationstation.stationapi.api.common.item;

import net.minecraft.util.io.CompoundTag;

import java.util.function.*;

public interface ItemWithEntity {

    Supplier<ItemEntity> getItemEntityFactory();

    Function<CompoundTag, ItemEntity> getItemEntityNBTFactory();
}
