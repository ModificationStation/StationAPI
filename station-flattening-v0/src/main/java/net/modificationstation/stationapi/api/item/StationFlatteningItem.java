package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.ApiStatus;

public interface StationFlatteningItem extends RemappableRawIdHolder, ItemConvertible, ItemStrengthWithBlockState {

    @Override
    @ApiStatus.Internal
    default void setRawId(int rawId) {
        Util.assertImpl();
    }

    @Override
    default ItemBase asItem() {
        return Util.assertImpl();
    }

    default RegistryEntry.Reference<ItemBase> getRegistryEntry() {
        return Util.assertImpl();
    }

    @Override
    default boolean isSuitableFor(ItemInstance itemStack, BlockState state) {
        return Util.assertImpl();
    }

    @Override
    default float getMiningSpeedMultiplier(ItemInstance itemStack, BlockState state) {
        return Util.assertImpl();
    }
}
