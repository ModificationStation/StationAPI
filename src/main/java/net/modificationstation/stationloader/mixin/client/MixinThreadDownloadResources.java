package net.modificationstation.stationloader.mixin.client;

import net.minecraft.client.util.ThreadDownloadResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThreadDownloadResources.class)
public class MixinThreadDownloadResources {

    @Inject(method = "run", at = @At("HEAD"), remap = false)
    public void cancel(CallbackInfo ci) {
        ci.cancel();
    }

}
