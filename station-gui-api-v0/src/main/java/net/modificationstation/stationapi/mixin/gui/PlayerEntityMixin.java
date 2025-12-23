package net.modificationstation.stationapi.mixin.gui;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "closeHandledScreen", at = @At("HEAD"), cancellable = true)
    private void stationapi_fixPrematureScreenHandlerReset(CallbackInfo ci) {
        ci.cancel();
    }

}
