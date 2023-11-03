package net.modificationstation.stationapi.mixin.entity.server;

import net.minecraft.class_488;
import net.minecraft.class_80;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.server.event.entity.TrackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_488.class)
public abstract class MixinServerEntityTracker {

    @Shadow private class_80 entityHashes;

    @Inject(method = "syncEntity(Lnet/minecraft/entity/EntityBase;)V", at = @At("RETURN"))
    private void afterVanillaEntries(Entity arg, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                TrackEntityEvent.builder()
                        .entityTracker((class_488) (Object) this)
                        .trackedEntities(entityHashes)
                        .entityToTrack(arg)
                        .build()
        );
    }
}
