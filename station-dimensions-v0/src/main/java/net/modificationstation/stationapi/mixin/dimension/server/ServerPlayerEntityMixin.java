package net.modificationstation.stationapi.mixin.dimension.server;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.PlayerManager;
import net.modificationstation.stationapi.api.entity.HasTeleportationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin implements HasTeleportationManager {
    @Redirect(
            method = "playerTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_166;changePlayerDimension(Lnet/minecraft/entity/player/ServerPlayerEntity;)V"
            )
    )
    private void stationapi_overrideSwitchDimensions(PlayerManager serverPlayerConnectionManager, ServerPlayerEntity serverPlayer) {
        //noinspection DataFlowIssue
        getTeleportationManager().switchDimension((ServerPlayerEntity) (Object) this);
    }
}
