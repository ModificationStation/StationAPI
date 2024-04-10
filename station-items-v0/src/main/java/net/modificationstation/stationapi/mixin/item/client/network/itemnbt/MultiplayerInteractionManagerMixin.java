package net.modificationstation.stationapi.mixin.item.client.network.itemnbt;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.MultiplayerInteractionManager;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.network.packet.c2s.play.StationClickSlotC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiplayerInteractionManager.class)
class MultiplayerInteractionManagerMixin {
    @Shadow private ClientNetworkHandler networkHandler;

    @WrapOperation(
            method = "clickSlot",
            at = @At(
                    value = "NEW",
                    target = "(IIIZLnet/minecraft/item/ItemStack;S)Lnet/minecraft/network/packet/c2s/play/ClickSlotC2SPacket;"
            )
    )
    private ClickSlotC2SPacket stationapi_redirectClickSlotPacket(int syncId, int slot, int button, boolean holdingShift, ItemStack stack, short actionType, Operation<ClickSlotC2SPacket> original) {
        return ((ModdedPacketHandler) networkHandler).isModded() ?
                new StationClickSlotC2SPacket(syncId, slot, button, holdingShift, stack, actionType) :
                original.call(syncId, slot, button, holdingShift, stack, actionType);
    }
}
