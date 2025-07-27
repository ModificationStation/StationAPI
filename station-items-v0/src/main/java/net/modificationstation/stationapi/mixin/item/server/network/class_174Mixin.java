package net.modificationstation.stationapi.mixin.item.server.network;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ItemEntitySpawnS2CPacket;
import net.minecraft.server.entity.EntityTrackerEntry;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationEntityEquipmentUpdateS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationItemEntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityTrackerEntry.class)
class class_174Mixin {
    @Redirect(
            method = "method_601",
            at = @At(
                    value = "NEW",
                    target = "(IILnet/minecraft/item/ItemStack;)Lnet/minecraft/network/packet/s2c/play/EntityEquipmentUpdateS2CPacket;"
            )
    )
    private EntityEquipmentUpdateS2CPacket stationapi_redirectEntityEquipmentUpdatePacket(int entityId, int slot, ItemStack stack) {
        return new StationEntityEquipmentUpdateS2CPacket(entityId, slot, stack);
    }

    @Redirect(
            method = "method_600",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/entity/ItemEntity;)Lnet/minecraft/network/packet/s2c/play/ItemEntitySpawnS2CPacket;"
            )
    )
    private ItemEntitySpawnS2CPacket stationapi_redirectItemEntitySpawnPacket(ItemEntity itemEntity) {
        return new StationItemEntitySpawnS2CPacket(itemEntity);
    }
}
