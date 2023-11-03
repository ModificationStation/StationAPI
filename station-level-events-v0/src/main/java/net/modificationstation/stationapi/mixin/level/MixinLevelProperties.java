package net.modificationstation.stationapi.mixin.level;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.LevelPropertiesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldProperties.class)
public class MixinLevelProperties {

    @Inject(method = "<init>(Lnet/minecraft/util/io/CompoundTag;)V", at = @At("RETURN"))
    private void onLoadFromTag(NbtCompound arg, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                LevelPropertiesEvent.Load.builder()
                        .levelProperties((WorldProperties) (Object) this)
                        .tag(arg)
                        .build()
        );
    }

    @Inject(method = "updateProperties(Lnet/minecraft/util/io/CompoundTag;Lnet/minecraft/util/io/CompoundTag;)V", at = @At("RETURN"))
    private void onSaveToTag(NbtCompound arg, NbtCompound arg1, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                LevelPropertiesEvent.Save.builder()
                        .levelProperties((WorldProperties) (Object) this)
                        .tag(arg)
                        .spPlayerData(arg1)
                        .build()
        );
    }
}
