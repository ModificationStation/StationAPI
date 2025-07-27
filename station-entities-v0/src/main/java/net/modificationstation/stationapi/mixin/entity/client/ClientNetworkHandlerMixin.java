package net.modificationstation.stationapi.mixin.entity.client;

import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.ClientWorld;
import net.modificationstation.stationapi.api.client.entity.factory.EntityWorldAndPosFactory;
import net.modificationstation.stationapi.api.client.registry.EntityHandlerRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.modificationstation.stationapi.api.util.Identifier.of;

@Mixin(ClientNetworkHandler.class)
class ClientNetworkHandlerMixin {
    @Shadow
    private ClientWorld world;
    @Unique
    private double
            capturedX,
            capturedY,
            capturedZ;

    @Inject(
            method = "onEntitySpawn",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;z:I",
                    opcode = Opcodes.GETFIELD,
                    shift = At.Shift.BY,
                    by = 5
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_captureCoords(EntitySpawnS2CPacket packet, CallbackInfo ci, double var2, double var4, double var6) {
        capturedX = var2;
        capturedY = var4;
        capturedZ = var6;
    }

    @ModifyVariable(
            method = "onEntitySpawn",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 0
            )
    )
    private Entity stationapi_onEntitySpawn(Entity entity, EntitySpawnS2CPacket packet) {
        EntityWorldAndPosFactory entityHandler = EntityHandlerRegistry.INSTANCE.get(of(String.valueOf(packet.entityType)));
        if (entityHandler != null)
            entity = entityHandler.create(world, capturedX, capturedY, capturedZ);
        return entity;
    }
}
