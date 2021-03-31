package net.modificationstation.stationapi.mixin.server;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayerPacketHandler.class)
public class MixinServerPlayerPacketHandler {

    @Shadow
    private ServerPlayer serverPlayer;

    @ModifyConstant(method = {"method_1426(Lnet/minecraft/packet/Id14Packet;)V", "method_1422(Lnet/minecraft/packet/Id7Packet;)V"}, constant = @Constant(doubleValue = 36))
    private double getBlockReach(double originalReach) {
        return Math.pow(StationAPI.EVENT_BUS.post(new PlayerEvent.Reach(serverPlayer, HitType.TILE, Math.sqrt(originalReach))).currentReach, 2);
    }
}
