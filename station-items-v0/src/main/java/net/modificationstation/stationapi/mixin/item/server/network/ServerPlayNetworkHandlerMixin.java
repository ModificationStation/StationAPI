package net.modificationstation.stationapi.mixin.item.server.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.impl.packet.StationScreenHandlerSlotUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
class ServerPlayNetworkHandlerMixin {
    @Redirect(
            method = "onPlayerInteractBlock",
            at = @At(
                    value = "NEW",
                    target = "(IILnet/minecraft/item/ItemStack;)Lnet/minecraft/network/packet/s2c/play/ScreenHandlerSlotUpdateS2CPacket;"
            )
    )
    private ScreenHandlerSlotUpdateS2CPacket stationapi_redirectSlotUpdatePacket(int slot, int stack, ItemStack itemStack) {
        return new StationScreenHandlerSlotUpdateS2CPacket(slot, stack, itemStack);
    }
}
