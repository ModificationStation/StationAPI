package net.modificationstation.stationapi.mixin.item.server;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.hit.HitResultType;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayerPacketHandler {

    @Shadow
    private ServerPlayerEntity serverPlayer;

    @ModifyConstant(method = "onPlayerDigging(Lnet/minecraft/packet/play/PlayerDigging0xEC2SPacket;)V", constant = @Constant(doubleValue = 36))
    private double getBlockReach(double originalReach) {
        return Math.pow(StationAPI.EVENT_BUS.post(
                PlayerEvent.Reach.builder()
                        .player(serverPlayer)
                        .type(HitResultType.BLOCK)
                        .currentReach(Math.sqrt(originalReach))
                        .build()
        ).currentReach, 2);
    }

    @ModifyConstant(method = "onEntityInteract(Lnet/minecraft/packet/play/EntityInteract0x7C2SPacket;)V", constant = @Constant(doubleValue = 36))
    private double getEntityReach(double originalReach) {
        return Math.pow(StationAPI.EVENT_BUS.post(
                PlayerEvent.Reach.builder()
                        .player(serverPlayer)
                        .type(HitResultType.ENTITY)
                        .currentReach(Math.sqrt(originalReach))
                        .build()
        ).currentReach, 2);
    }
}
