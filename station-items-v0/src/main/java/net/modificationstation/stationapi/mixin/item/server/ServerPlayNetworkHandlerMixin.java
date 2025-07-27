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
class ServerPlayNetworkHandlerMixin {
    @Shadow
    private ServerPlayerEntity player;

    @ModifyConstant(
            method = "handlePlayerAction",
            constant = @Constant(doubleValue = 36)
    )
    private double stationapi_getBlockReach(double originalReach) {
        return Math.pow(StationAPI.EVENT_BUS.post(
                PlayerEvent.Reach.builder()
                        .player(player)
                        .type(HitResultType.BLOCK)
                        .currentReach(Math.sqrt(originalReach))
                        .build()
        ).currentReach, 2);
    }

    @ModifyConstant(
            method = "handleInteractEntity",
            constant = @Constant(doubleValue = 36)
    )
    private double stationapi_getEntityReach(double originalReach) {
        return Math.pow(StationAPI.EVENT_BUS.post(
                PlayerEvent.Reach.builder()
                        .player(player)
                        .type(HitResultType.ENTITY)
                        .currentReach(Math.sqrt(originalReach))
                        .build()
        ).currentReach, 2);
    }
}
