package net.modificationstation.stationloader.mixin.client.debugfixes;

import net.fabricmc.loader.FabricLoader;
import net.minecraft.client.util.ThreadDownloadResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ThreadDownloadResources.class, priority = 999999)
public class DestroyStupidResource404Errors {

    @Inject(method = "run", at = @At(value = "HEAD"), remap = false, cancellable = true, require = 0)
    public void run(CallbackInfo ci) {
        if (FabricLoader.INSTANCE.isDevelopmentEnvironment()) {
            ci.cancel();
        }
    }
}
