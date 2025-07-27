package net.modificationstation.stationapi.mixin.entity.server;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.entity.EntityTrackerEntry;
import net.modificationstation.stationapi.api.server.entity.CustomSpawnDataProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityTrackerEntry.class)
class TrackedEntityMixin {
    @Shadow
    public Entity field_597;

    @Inject(
            method = "method_600",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_getSpawnData(CallbackInfoReturnable<Packet> cir) {
        if (this.field_597 instanceof CustomSpawnDataProvider provider)
            cir.setReturnValue(provider.getSpawnData());
    }
}
