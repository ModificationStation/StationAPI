package net.modificationstation.stationapi.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.container.slot.ItemCraftedEvent;
import net.modificationstation.stationapi.api.event.container.slot.ItemUsedInCraftingEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
class CraftingResultMixin {
    @Shadow
    private PlayerEntity player;

    @Shadow
    @Final
    private Inventory input;

    @Inject(
            method = "onTakeItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V",
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private void stationapi_onCrafted(
            ItemStack arg, CallbackInfo ci,
            @Local(index = 2) int var2, @Local(index = 3) ItemStack var3
    ) {
        if (var3 != null) {
            StationAPI.EVENT_BUS.post(
                    ItemUsedInCraftingEvent.builder()
                            .player(player)
                            .craftingMatrix(input)
                            .itemOrdinal(var2)
                            .itemCrafted(arg)
                            .itemUsed(var3)
                            .build()
            );
        }
    }

    @Inject(
            method = "onTakeItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;onCraft(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_onTake(ItemStack stack, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                ItemCraftedEvent.builder()
                        .player(player)
                        .craftingMatrix(input)
                        .itemCrafted(stack).build()
        );
    }

}
