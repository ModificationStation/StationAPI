package net.modificationstation.stationapi.mixin.sortme.common;

import net.minecraft.container.slot.CraftingResult;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.common.event.container.slot.ItemUsedInCraftingEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CraftingResult.class)
public class MixinCraftingResult {

    @Shadow
    private PlayerBase player;

    @Shadow
    @Final
    private InventoryBase resultInventory;

    @Inject(method = "onCrafted(Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/InventoryBase;setInventoryItem(ILnet/minecraft/item/ItemInstance;)V", shift = At.Shift.BY, by = 2), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onCrafted(ItemInstance arg, CallbackInfo ci, int var2, ItemInstance var3) {
        StationAPI.EVENT_BUS.post(new ItemUsedInCraftingEvent(player, resultInventory, var2, var3, arg));
    }
}
