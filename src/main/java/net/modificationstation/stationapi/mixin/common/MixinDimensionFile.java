package net.modificationstation.stationapi.mixin.common;

import net.minecraft.level.LevelProperties;
import net.minecraft.level.dimension.DimensionFile;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.event.level.LoadLevelPropertiesOnLevelInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;

@Mixin(DimensionFile.class)
public class MixinDimensionFile {

    @Inject(method = "getLevelProperties()Lnet/minecraft/level/LevelProperties;", at = @At(value = "RETURN", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onLoadLevelPropertiesOnLevelInit(CallbackInfoReturnable<LevelProperties> cir, File file, CompoundTag var7, CompoundTag var8) {
        StationAPI.EVENT_BUS.post(new LoadLevelPropertiesOnLevelInit(cir.getReturnValue(), var8));
    }

    @Inject(method = "getLevelProperties()Lnet/minecraft/level/LevelProperties;", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onLoadLevelPropertiesOnLevelInit_old(CallbackInfoReturnable<LevelProperties> cir, File file, CompoundTag var2, CompoundTag var3) {
        StationAPI.EVENT_BUS.post(new LoadLevelPropertiesOnLevelInit(cir.getReturnValue(), var3));
    }
}
