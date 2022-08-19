package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningItemBase extends ItemStrengthWithBlockState {

    @Override
    default boolean isSuitableFor(ItemInstance itemStack, BlockState state) {
        return Util.assertImpl();
    }

    @Override
    default float getMiningSpeedMultiplier(ItemInstance itemStack, BlockState state) {
        return Util.assertImpl();
    }
}
