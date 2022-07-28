package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.Fire;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.FireBurnableRegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Fire.class)
public abstract class MixinFire {

    @Shadow protected abstract void addBurnable(int i, int j, int k);

    @Inject(
            method = "init()V",
            at = @At("RETURN")
    )
    private void postBurnableRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(FireBurnableRegisterEvent.builder().addBurnable(this::addBurnable).build());
    }
}
