package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.entity.player.StationFlatteningPlayerInventory;
import net.modificationstation.stationapi.api.event.entity.player.IsPlayerUsingEffectiveToolEvent;
import net.modificationstation.stationapi.api.event.entity.player.PlayerStrengthOnBlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerInventory.class)
abstract class PlayerInventoryMixin implements StationFlatteningPlayerInventory {
    @Shadow public ItemStack[] main;

    @Shadow public int selectedSlot;

    @Shadow public abstract ItemStack getStack(int i);

    @Shadow public PlayerEntity player;

    @Override
    @Unique
    public float getBlockBreakingSpeed(BlockView blockView, BlockPos blockPos, BlockState state) {
        return StationAPI.EVENT_BUS.post(PlayerStrengthOnBlockEvent.builder()
                .player(player)
                .blockView(blockView)
                .blockPos(blockPos)
                .blockState(state)
                .resultProvider(() -> {
                    float var2 = 1.0F;
                    if (main[selectedSlot] != null)
                        var2 *= main[this.selectedSlot].getMiningSpeedMultiplier(player, blockView, blockPos, state);
                    return var2;
                })
                .build()
        ).resultProvider.getAsFloat();
    }

    @Override
    @Unique
    public boolean canHarvest(BlockView blockView, BlockPos blockPos, BlockState state) {
        return StationAPI.EVENT_BUS.post(IsPlayerUsingEffectiveToolEvent.builder()
                .player(player)
                .blockView(blockView)
                .blockPos(blockPos)
                .blockState(state)
                .resultProvider(() -> {
                    if (state.isToolRequired()) {
                        ItemStack var2 = getStack(this.selectedSlot);
                        return var2 != null && var2.isSuitableFor(player, blockView, blockPos, state);
                    } else return true;
                })
                .build()
        ).resultProvider.getAsBoolean();
    }
}
