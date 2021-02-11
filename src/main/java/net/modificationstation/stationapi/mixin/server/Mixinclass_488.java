package net.modificationstation.stationapi.mixin.server;

import net.minecraft.class_488;
import net.minecraft.class_80;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.server.event.network.TrackEntity;
import net.modificationstation.stationapi.impl.common.StationAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_488.class)
public abstract class Mixinclass_488 {

    @Shadow private class_80 field_2005;

    @Inject(method = "method_1665(Lnet/minecraft/entity/EntityBase;)V", at = @At("RETURN"))
    private void afterVanillaEntries(EntityBase arg, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new TrackEntity((class_488) (Object) this, field_2005, arg));
    }
}
