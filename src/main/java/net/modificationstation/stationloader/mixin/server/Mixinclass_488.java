package net.modificationstation.stationloader.mixin.server;

import net.minecraft.class_488;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationloader.api.server.entity.CustomTracking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_488.class)
public abstract class Mixinclass_488 {

    @Shadow
    public abstract void method_1667(EntityBase arg, int i, int j, boolean flag);

    @Inject(method = "method_1665(Lnet/minecraft/entity/EntityBase;)V", at = @At("TAIL"))
    private void afterVanillaEntries(EntityBase arg, CallbackInfo ci) {
        if (arg instanceof CustomTracking) {
            CustomTracking track = (CustomTracking) arg;
            method_1667(arg, track.getTrackingDistance(), track.getUpdateFrequency(), track.sendVelocity());
        }
    }
}
