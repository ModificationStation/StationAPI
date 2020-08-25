package net.modificationstation.stationloader.mixin.client.debugfixes;

import net.fabricmc.loader.FabricLoader;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.LoginThread.class, priority = 999999)
public class MixinFixThreadCheckHasPaid {

    @Inject(method = "run", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void run(CallbackInfo info) {
        if (FabricLoader.INSTANCE.isDevelopmentEnvironment()) {
            info.cancel();
        }
    }
}