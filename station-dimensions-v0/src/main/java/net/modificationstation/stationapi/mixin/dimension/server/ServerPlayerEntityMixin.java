package net.modificationstation.stationapi.mixin.dimension.server;

import net.minecraft.class_166;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.modificationstation.stationapi.api.entity.HasTeleportationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin implements HasTeleportationManager {
    @Redirect(
            method = "method_313",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_166;method_578(Lnet/minecraft/entity/player/ServerPlayerEntity;)V"
            )
    )
    private void stationapi_overrideSwitchDimensions(class_166 serverPlayerConnectionManager, ServerPlayerEntity serverPlayer) {
        //noinspection DataFlowIssue
        getTeleportationManager().switchDimension((ServerPlayerEntity) (Object) this);
    }
}
