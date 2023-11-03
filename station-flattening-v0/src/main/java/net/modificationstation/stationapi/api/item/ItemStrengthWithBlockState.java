package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.BlockState;

public interface ItemStrengthWithBlockState {

    boolean isSuitableFor(ItemStack itemStack, BlockState state);

    float getMiningSpeedMultiplier(ItemStack itemStack, BlockState state);
}
