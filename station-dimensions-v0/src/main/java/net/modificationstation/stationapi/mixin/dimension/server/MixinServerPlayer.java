package net.modificationstation.stationapi.mixin.dimension.server;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.ServerPlayerConnectionManager;
import net.modificationstation.stationapi.api.entity.HasTeleportationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer implements HasTeleportationManager {

    @Redirect(
            method = "tick(Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/ServerPlayerConnectionManager;sendToOppositeDimension(Lnet/minecraft/entity/player/ServerPlayer;)V"
            )
    )
    private void overrideSwitchDimensions(ServerPlayerConnectionManager serverPlayerConnectionManager, ServerPlayer serverPlayer) {
        getTeleportationManager().switchDimension((ServerPlayer) (Object) this);
    }
}
