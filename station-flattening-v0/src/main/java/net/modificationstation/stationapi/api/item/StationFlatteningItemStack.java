package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningItemStack extends ItemStackStrengthWithBlockState {

    @Override
    default boolean isSuitableFor(BlockState state) {
        return Util.assertImpl();
    }

    @Override
    default float getMiningSpeedMultiplier(BlockState state) {
        return Util.assertImpl();
    }

    /**
     * {@return whether the item is {@code item}}
     */
    default boolean isOf(ItemBase item) {
        return Util.assertImpl();
    }
}
