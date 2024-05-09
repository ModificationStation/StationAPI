package net.modificationstation.stationapi.mixin.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.celestial.CelestialRegisterEvent;
import net.modificationstation.stationapi.api.event.world.WorldPropertiesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldProperties.class)
public class WorldPropertiesMixin {
    @Inject(
            method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V",
            at = @At("RETURN")
    )
    private void stationapi_onLoadFromTag(NbtCompound arg, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                WorldPropertiesEvent.Load.builder()
                        .worldProperties((WorldProperties) (Object) this)
                        .nbt(arg)
                        .build()
        );
    }

    @Inject(
            method = "updateProperties",
            at = @At("RETURN")
    )
    private void stationapi_onSaveToTag(NbtCompound arg, NbtCompound arg1, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                WorldPropertiesEvent.Save.builder()
                        .worldProperties((WorldProperties) (Object) this)
                        .nbt(arg)
                        .spPlayerNbt(arg1)
                        .build()
        );
    }

    @Inject(
            method = "setTime",
            at = @At("HEAD")
    )
    private void stationapi_postCelestialEvent(CallbackInfo ci) { // This gets called more than once. Does not really make sense with the way the events work
        StationAPI.EVENT_BUS.post(
                CelestialRegisterEvent.builder().build()
        );
    }
}
