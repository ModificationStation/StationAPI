package net.modificationstation.stationapi.mixin.effects;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.impl.effect.packet.SendAllEffectsPlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginNetworkHandler.class)
public class MixinServerLoginNetworkHandler {
    @Inject(method = "accept", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/entity/player/ServerPlayerEntity;method_317()V",
        shift = Shift.BEFORE
    ))
    private void stationapi_updatePlayerEffects(LoginHelloPacket packet, CallbackInfo info, @Local ServerPlayerEntity player) {
        var effects = player.getServerEffects();
        if (effects == null) return;
        PacketHelper.sendTo(player, new SendAllEffectsPlayerPacket(effects));
    }
}
