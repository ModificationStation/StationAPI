package net.modificationstation.stationapi.mixin.item.server.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationInventoryS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationScreenHandlerSlotUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
class ServerPlayerEntityMixin {
    @Redirect(
            method = {
                    "method_2100",
                    "method_2101",
                    "method_319"
            },
            at = @At(
                    value = "NEW",
                    target = "(IILnet/minecraft/item/ItemStack;)Lnet/minecraft/network/packet/s2c/play/ScreenHandlerSlotUpdateS2CPacket;"
            )
    )
    private ScreenHandlerSlotUpdateS2CPacket stationapi_redirectSlotUpdatePacket(int slot, int stack, ItemStack itemStack) {
        return new StationScreenHandlerSlotUpdateS2CPacket(slot, stack, itemStack);
    }

    @Redirect(
            method = "method_2101",
            at = @At(
                    value = "NEW",
                    target = "(ILjava/util/List;)Lnet/minecraft/network/packet/s2c/play/InventoryS2CPacket;"
            )
    )
    private InventoryS2CPacket stationapi_redirectInventoryPacket(int contents, List<ItemStack> list) {
        return new StationInventoryS2CPacket(contents, list);
    }
}
