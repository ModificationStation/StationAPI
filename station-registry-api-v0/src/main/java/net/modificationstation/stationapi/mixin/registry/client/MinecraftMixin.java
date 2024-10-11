package net.modificationstation.stationapi.mixin.registry.client;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
class MinecraftMixin {
    @Inject(
            method = "run",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;init()V", shift = At.Shift.AFTER)
    )
    private void stationapi_freeze(CallbackInfo ci) {
        Registries.bootstrap();
    }
}
