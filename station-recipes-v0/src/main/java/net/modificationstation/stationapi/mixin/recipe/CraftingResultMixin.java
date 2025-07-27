package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.container.slot.ItemUsedInCraftingEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CraftingResultSlot.class)
class CraftingResultMixin {
    @Shadow
    private PlayerEntity player;

    @Shadow
    @Final
    private Inventory input;

    @Inject(
            method = "onCrafted",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V",
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_onCrafted(ItemStack arg, CallbackInfo ci, int var2, ItemStack var3) {
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
