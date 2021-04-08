package net.modificationstation.stationapi.mixin.sortme.client;

import net.minecraft.client.level.ClientLevel;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.packet.play.EntitySpawnS2C;
import net.modificationstation.stationapi.api.common.entity.EntityHandlerRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import uk.co.benjiweber.expressions.functions.QuadFunction;

import java.util.*;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Shadow
    private ClientLevel level;
    private double capturedX;
    private double capturedY;
    private double capturedZ;

    @Inject(method = "handleEntitySpawn(Lnet/minecraft/packet/play/EntitySpawnS2C;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/packet/play/EntitySpawnS2C;z:I", opcode = Opcodes.GETFIELD, shift = At.Shift.BY, by = 5), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureCoords(EntitySpawnS2C packet, CallbackInfo ci, double var2, double var4, double var6) {
        capturedX = var2;
        capturedY = var4;
        capturedZ = var6;
    }

    @ModifyVariable(method = "handleEntitySpawn(Lnet/minecraft/packet/play/EntitySpawnS2C;)V", index = 8, at = @At("LOAD"))
    private EntityBase onEntitySpawn(EntityBase entity, EntitySpawnS2C packet) {
        Optional<QuadFunction<Level, Double, Double, Double, EntityBase>> entityHandler = EntityHandlerRegistry.INSTANCE.getByIdentifier(Identifier.of(ModID.of("minecraft"), String.valueOf(packet.id)));
        if (entityHandler.isPresent())
            entity = entityHandler.get().apply(level, capturedX, capturedY, capturedZ);
        return entity;
    }
}
