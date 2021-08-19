package net.modificationstation.stationapi.mixin.item.server;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayerPacketHandler.class)
public class MixinServerPlayerPacketHandler {

    @Shadow
    private ServerPlayer serverPlayer;

    @ModifyConstant(method = "onPlayerDigging(Lnet/minecraft/packet/play/PlayerDigging0xEC2SPacket;)V", constant = @Constant(doubleValue = 36))
    private double getBlockReach(double originalReach) {
        return Math.pow(StationAPI.EVENT_BUS.post(new PlayerEvent.Reach(serverPlayer, HitType.TILE, Math.sqrt(originalReach))).currentReach, 2);
    }

    @ModifyConstant(method = "onEntityInteract(Lnet/minecraft/packet/play/EntityInteract0x7C2SPacket;)V", constant = @Constant(doubleValue = 36))
    private double getEntityReach(double originalReach) {
        return Math.pow(StationAPI.EVENT_BUS.post(new PlayerEvent.Reach(serverPlayer, HitType.ENTITY, Math.sqrt(originalReach))).currentReach, 2);
    }
}
