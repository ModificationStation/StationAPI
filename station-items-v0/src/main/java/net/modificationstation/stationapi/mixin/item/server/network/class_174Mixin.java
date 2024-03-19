package net.modificationstation.stationapi.mixin.item.server.network;

import net.minecraft.class_174;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.packet.s2c.play.ItemEntitySpawnS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationItemEntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_174.class)
class class_174Mixin {
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
