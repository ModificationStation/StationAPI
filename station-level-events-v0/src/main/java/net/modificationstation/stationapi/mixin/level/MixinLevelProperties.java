package net.modificationstation.stationapi.mixin.level;

import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.LevelPropertiesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelProperties.class)
public class MixinLevelProperties {

    @Inject(method = "<init>(Lnet/minecraft/util/io/CompoundTag;)V", at = @At("RETURN"))
    private void onLoadFromTag(CompoundTag arg, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new LevelPropertiesEvent.Load((LevelProperties) (Object) this, arg));
    }

    @Inject(method = "updateProperties(Lnet/minecraft/util/io/CompoundTag;Lnet/minecraft/util/io/CompoundTag;)V", at = @At("RETURN"))
    private void onSaveToTag(CompoundTag arg, CompoundTag arg1, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new LevelPropertiesEvent.Save((LevelProperties) (Object) this, arg, arg1));
    }
}
