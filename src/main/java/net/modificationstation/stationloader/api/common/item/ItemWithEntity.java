package net.modificationstation.stationloader.api.common.item;

import net.minecraft.util.io.CompoundTag;

import java.util.function.Function;
import java.util.function.Supplier;

public interface ItemWithEntity {

    Supplier<ItemEntity> getItemEntityFactory();

    Function<CompoundTag, ItemEntity> getItemEntityNBTFactory();
}
