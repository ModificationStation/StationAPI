package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.entity.player.StationFlatteningPlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerInventory.class)
abstract class PlayerInventoryMixin implements StationFlatteningPlayerInventory {
    @Shadow public ItemStack[] main;

    @Shadow public int selectedSlot;

    @Shadow public abstract ItemStack getStack(int i);

    @Override
    @Unique
    public float getBlockBreakingSpeed(BlockState state) {
        float var2 = 1.0F;
        if (main[selectedSlot] != null)
            var2 *= main[this.selectedSlot].getMiningSpeedMultiplier(state);
        return var2;
    }

    @Override
    @Unique
    public boolean canHarvest(BlockState state) {
        if (state.isToolRequired()) {
            ItemStack var2 = getStack(this.selectedSlot);
            return var2 != null && var2.isSuitableFor(state);
        } else return true;
    }
}
