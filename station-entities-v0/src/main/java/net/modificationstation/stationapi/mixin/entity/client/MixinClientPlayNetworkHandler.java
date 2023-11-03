package net.modificationstation.stationapi.mixin.entity.client;

import net.modificationstation.stationapi.api.registry.EntityHandlerRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import uk.co.benjiweber.expressions.function.QuadFunction;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

import net.minecraft.class_454;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

@Mixin(ClientNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Shadow
    private class_454 level;
    @Unique
    private double
            capturedX,
            capturedY,
            capturedZ;

    @Inject(
            method = "onEntitySpawn(Lnet/minecraft/packet/play/EntitySpawn0x17S2CPacket;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/packet/play/EntitySpawn0x17S2CPacket;z:I",
                    opcode = Opcodes.GETFIELD,
                    shift = At.Shift.BY,
                    by = 5
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void captureCoords(EntitySpawnS2CPacket packet, CallbackInfo ci, double var2, double var4, double var6) {
        capturedX = var2;
        capturedY = var4;
        capturedZ = var6;
    }

    @ModifyVariable(
            method = "onEntitySpawn(Lnet/minecraft/packet/play/EntitySpawn0x17S2CPacket;)V",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 0
            )
    )
    private Entity onEntitySpawn(Entity entity, EntitySpawnS2CPacket packet) {
        QuadFunction<World, Double, Double, Double, Entity> entityHandler = EntityHandlerRegistry.INSTANCE.get(of(String.valueOf(packet.entityType)));
        if (entityHandler != null)
            entity = entityHandler.apply(level, capturedX, capturedY, capturedZ);
        return entity;
    }
}
