package net.modificationstation.stationapi.mixin.entity.client;

import net.minecraft.client.level.ClientLevel;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.packet.play.EntitySpawn0x17S2CPacket;
import net.modificationstation.stationapi.api.registry.EntityHandlerRegistry;
import net.modificationstation.stationapi.api.registry.ModID;
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

import java.util.Optional;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Shadow
    private ClientLevel level;
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
    private void captureCoords(EntitySpawn0x17S2CPacket packet, CallbackInfo ci, double var2, double var4, double var6) {
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
    private EntityBase onEntitySpawn(EntityBase entity, EntitySpawn0x17S2CPacket packet) {
        Optional<QuadFunction<Level, Double, Double, Double, EntityBase>> entityHandler = EntityHandlerRegistry.INSTANCE.get(of(ModID.of("minecraft"), String.valueOf(packet.type)));
        if (entityHandler.isPresent())
            entity = entityHandler.get().apply(level, capturedX, capturedY, capturedZ);
        return entity;
    }
}
