package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.entity.player.PlayerStrengthWithBlockState;
import net.modificationstation.stationapi.api.item.ItemStackStrengthWithBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerInventory.class)
public abstract class MixinPlayerInventory implements PlayerStrengthWithBlockState {

    @Shadow public ItemInstance[] main;

    @Shadow public int selectedHotbarSlot;

    @Shadow public abstract ItemInstance getInventoryItem(int i);

    @Override
    public float getBlockBreakingSpeed(BlockState state) {
        float var2 = 1.0F;
        if (main[selectedHotbarSlot] != null) //noinspection ConstantConditions
            var2 *= ItemStackStrengthWithBlockState.class.cast(main[this.selectedHotbarSlot]).getMiningSpeedMultiplier(state);
        return var2;
    }

    @Override
    public boolean canHarvest(BlockState state) {
        if (state.isToolRequired()) {
            ItemInstance var2 = getInventoryItem(this.selectedHotbarSlot);
            return var2 != null && ItemStackStrengthWithBlockState.class.cast(var2).isSuitableFor(state);
        } else return true;
    }
}
