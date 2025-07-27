package net.modificationstation.stationapi.mixin.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.storage.AlphaWorldStorage;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.world.WorldPropertiesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;

@Mixin(AlphaWorldStorage.class)
class DimensionFileMixin {
    @Inject(
            method = "loadProperties",
            at = @At(
                    value = "RETURN",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onLoadWorldPropertiesOnLevelInit(CallbackInfoReturnable<WorldProperties> cir, File file, NbtCompound var7, NbtCompound var8) {
        StationAPI.EVENT_BUS.post(
                WorldPropertiesEvent.LoadOnWorldInit.builder()
                        .worldProperties(cir.getReturnValue())
                        .nbt(var8)
                        .build()
        );
    }

    @Inject(
            method = "loadProperties",
            at = @At(
                    value = "RETURN",
                    ordinal = 1
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onLoadWorldPropertiesOnWorldInit_old(CallbackInfoReturnable<WorldProperties> cir, File file, NbtCompound var2, NbtCompound var3) {
        StationAPI.EVENT_BUS.post(
                WorldPropertiesEvent.LoadOnWorldInit.builder()
                        .worldProperties(cir.getReturnValue())
                        .nbt(var3)
                        .build()
        );
    }
}
