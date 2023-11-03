package net.modificationstation.stationapi.mixin.level;

import net.minecraft.class_81;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.LevelPropertiesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;

@Mixin(class_81.class)
public class MixinDimensionFile {

    @Inject(method = "getLevelProperties()Lnet/minecraft/level/LevelProperties;", at = @At(value = "RETURN", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onLoadLevelPropertiesOnLevelInit(CallbackInfoReturnable<WorldProperties> cir, File file, NbtCompound var7, NbtCompound var8) {
        StationAPI.EVENT_BUS.post(
                LevelPropertiesEvent.LoadOnLevelInit.builder()
                        .levelProperties(cir.getReturnValue())
                        .tag(var8)
                        .build()
        );
    }

    @Inject(method = "getLevelProperties()Lnet/minecraft/level/LevelProperties;", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onLoadLevelPropertiesOnLevelInit_old(CallbackInfoReturnable<WorldProperties> cir, File file, NbtCompound var2, NbtCompound var3) {
        StationAPI.EVENT_BUS.post(
                LevelPropertiesEvent.LoadOnLevelInit.builder()
                        .levelProperties(cir.getReturnValue())
                        .tag(var3)
                        .build()
        );
    }
}
