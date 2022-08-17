package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemStrengthWithBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemBase.class)
public abstract class MixinItemBase implements ItemStrengthWithBlockState {

    @Shadow public abstract boolean isEffectiveOn(BlockBase arg);

    @Shadow public abstract float getStrengthOnBlock(ItemInstance arg, BlockBase arg2);

    @Override
    public boolean isSuitableFor(ItemInstance itemStack, BlockState state) {
        return isEffectiveOn(state.getBlock());
    }

    @Override
    public float getMiningSpeedMultiplier(ItemInstance itemStack, BlockState state) {
        return getStrengthOnBlock(itemStack, state.getBlock());
    }
}
