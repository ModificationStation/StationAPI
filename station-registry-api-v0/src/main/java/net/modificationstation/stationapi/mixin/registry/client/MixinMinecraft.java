package net.modificationstation.stationapi.mixin.registry.client;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(
            method = "run",
            at = @At("HEAD"),
            remap = false
    )
    private void freeze(CallbackInfo ci) {
        Registry.freezeRegistries();
    }
}
