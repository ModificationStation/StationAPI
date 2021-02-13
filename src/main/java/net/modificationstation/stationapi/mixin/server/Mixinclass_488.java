package net.modificationstation.stationapi.mixin.server;

import net.minecraft.entity.EntityBase;
import net.minecraft.network.EntityHashSet;
import net.minecraft.server.network.ServerEntityTracker;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.server.event.network.TrackEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntityTracker.class)
public abstract class Mixinclass_488 {

    @Shadow private EntityHashSet field_2005;

    @Inject(method = "method_1665(Lnet/minecraft/entity/EntityBase;)V", at = @At("RETURN"))
    private void afterVanillaEntries(EntityBase arg, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new TrackEntity((ServerEntityTracker) (Object) this, field_2005, arg));
    }
}
