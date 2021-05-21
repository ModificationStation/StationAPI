package net.modificationstation.stationapi.api.item.nbt;

import net.minecraft.util.io.CompoundTag;

import java.util.function.*;

// TODO: refactor. Maybe turn into a class (to match BlockWithEntity, although not required in item's case). Make it create the ItemEntity instead of providing the factories.
public interface ItemWithEntity {

    Supplier<ItemEntity> getItemEntityFactory();

    Function<CompoundTag, ItemEntity> getItemEntityNBTFactory();
}
