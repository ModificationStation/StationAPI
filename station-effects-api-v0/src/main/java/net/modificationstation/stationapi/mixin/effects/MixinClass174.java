package net.modificationstation.stationapi.mixin.effects;

import net.minecraft.class_174;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.impl.effect.packet.SendAllEffectsS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_174.class)
public class MixinClass174 {
    @Shadow public Entity field_597;
    
    @Inject(method = "method_601", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;method_835(Lnet/minecraft/network/packet/Packet;)V",
        shift = Shift.AFTER, ordinal = 0
    ))
    private void stationapi_updateEntities(ServerPlayerEntity player, CallbackInfo info) {
        var effects = field_597.getServerEffects();
        if (effects == null) return;
        PacketHelper.sendTo(player, new SendAllEffectsS2CPacket(field_597.id, effects));
    }
}
