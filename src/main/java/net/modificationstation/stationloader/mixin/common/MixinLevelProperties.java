package net.modificationstation.stationloader.mixin.common;

import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationloader.api.common.event.level.LoadLevelProperties;
import net.modificationstation.stationloader.api.common.event.level.SaveLevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelProperties.class)
public class MixinLevelProperties {

    @Inject(method = "<init>(Lnet/minecraft/util/io/CompoundTag;)V", at = @At("RETURN"))
    private void onLoadFromTag(CompoundTag arg, CallbackInfo ci) {
        LoadLevelProperties.EVENT.getInvoker().loadLevelProperties((LevelProperties) (Object) this, arg);
    }

    @Inject(method = "updateProperties(Lnet/minecraft/util/io/CompoundTag;Lnet/minecraft/util/io/CompoundTag;)V", at = @At("RETURN"))
    private void onSaveToTag(CompoundTag arg, CompoundTag arg1, CallbackInfo ci) {
        SaveLevelProperties.EVENT.getInvoker().saveLevelProperties((LevelProperties) (Object) this, arg, arg1);
    }
}
