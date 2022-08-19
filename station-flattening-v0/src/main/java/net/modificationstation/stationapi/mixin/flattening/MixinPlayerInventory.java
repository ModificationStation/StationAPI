package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.entity.player.StationFlatteningPlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerInventory.class)
public abstract class MixinPlayerInventory implements StationFlatteningPlayerInventory {

    @Shadow public ItemInstance[] main;

    @Shadow public int selectedHotbarSlot;

    @Shadow public abstract ItemInstance getInventoryItem(int i);

    @Override
    public float getBlockBreakingSpeed(BlockState state) {
        float var2 = 1.0F;
        if (main[selectedHotbarSlot] != null)
            var2 *= main[this.selectedHotbarSlot].getMiningSpeedMultiplier(state);
        return var2;
    }

    @Override
    public boolean canHarvest(BlockState state) {
        if (state.isToolRequired()) {
            ItemInstance var2 = getInventoryItem(this.selectedHotbarSlot);
            return var2 != null && var2.isSuitableFor(state);
        } else return true;
    }
}
