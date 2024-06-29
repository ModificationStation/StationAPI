package net.modificationstation.stationapi.mixin.effects;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.class_174;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.effect.packet.SendAllEffectsPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(class_174.class)
public class MixinClass174 {
	@Shadow public Entity field_597;
	
	@Inject(method = "method_601", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;method_835(Lnet/minecraft/network/packet/Packet;)V",
		shift = Shift.AFTER, ordinal = 0
	))
	private void stationapi_updateEntities(ServerPlayerEntity player, CallbackInfo info) {
		Collection<Pair<Identifier, Integer>> effects = field_597.getServerEffects();
		if (effects == null) return;
		PacketHelper.sendTo(player, new SendAllEffectsPacket(field_597.id, effects));
	}
}
