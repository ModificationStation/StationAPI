package net.modificationstation.stationapi.api.item.nbt;

import net.minecraft.util.io.CompoundTag;

import java.util.function.*;

@Deprecated
public interface ItemWithEntity {

    Supplier<ItemEntity> getItemEntityFactory();

    Function<CompoundTag, ItemEntity> getItemEntityNBTFactory();
}
