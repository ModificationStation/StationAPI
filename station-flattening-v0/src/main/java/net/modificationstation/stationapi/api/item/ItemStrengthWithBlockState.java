package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockState;

public interface ItemStrengthWithBlockState {

    boolean isSuitableFor(ItemInstance itemStack, BlockState state);

    float getMiningSpeedMultiplier(ItemInstance itemStack, BlockState state);
}
