package net.modificationstation.stationapi.mixin.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Shadow
    public ScreenHandler currentScreenHandler;

    @Inject(method = "closeHandledScreen", at = @At("HEAD"))
    private void stationapi_fixPrematureScreenHandlerReset(CallbackInfo ci) {
        currentScreenHandler.stationAPI$onClose((PlayerEntity) (Object) this);
    }

}
