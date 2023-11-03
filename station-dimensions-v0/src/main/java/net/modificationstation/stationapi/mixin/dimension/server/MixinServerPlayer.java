package net.modificationstation.stationapi.mixin.dimension.server;

import net.minecraft.class_166;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.modificationstation.stationapi.api.entity.HasTeleportationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayer implements HasTeleportationManager {

    @Redirect(
            method = "tick(Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/ServerPlayerConnectionManager;sendToOppositeDimension(Lnet/minecraft/entity/player/ServerPlayer;)V"
            )
    )
    private void overrideSwitchDimensions(class_166 serverPlayerConnectionManager, ServerPlayerEntity serverPlayer) {
        getTeleportationManager().switchDimension((ServerPlayerEntity) (Object) this);
    }
}
