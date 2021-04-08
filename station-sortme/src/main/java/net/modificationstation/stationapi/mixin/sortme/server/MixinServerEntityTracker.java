package net.modificationstation.stationapi.mixin.sortme.server;

import net.minecraft.entity.EntityBase;
import net.minecraft.network.EntityHashSet;
import net.minecraft.server.network.ServerEntityTracker;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.server.event.network.TrackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntityTracker.class)
public abstract class MixinServerEntityTracker {

    @Shadow private EntityHashSet entityHashes;

    @Inject(method = "syncEntity(Lnet/minecraft/entity/EntityBase;)V", at = @At("RETURN"))
    private void afterVanillaEntries(EntityBase arg, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new TrackEntityEvent((ServerEntityTracker) (Object) this, entityHashes, arg));
    }
}
