package net.modificationstation.stationapi.mixin.entity.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.ClientWorld;
import net.modificationstation.stationapi.api.client.entity.factory.EntityWorldAndPosFactory;
import net.modificationstation.stationapi.api.client.registry.EntityHandlerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static net.modificationstation.stationapi.api.util.Identifier.of;

@Mixin(ClientNetworkHandler.class)
class ClientNetworkHandlerMixin {
    @Shadow
    private ClientWorld world;

    @ModifyVariable(
            method = "onEntitySpawn",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 0
            )
    )
    private Entity stationapi_onEntitySpawn(
            Entity entity, EntitySpawnS2CPacket packet,
            @Local(index = 2) double trackedPosX,
            @Local(index = 4) double trackedPosY,
            @Local(index = 6) double trackedPosZ
    ) {
        EntityWorldAndPosFactory entityHandler = EntityHandlerRegistry.INSTANCE.get(of(String.valueOf(packet.entityType)));
        if (entityHandler != null)
            entity = entityHandler.create(world, trackedPosX, trackedPosY, trackedPosZ);
        return entity;
    }
}
