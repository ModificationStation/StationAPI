package net.modificationstation.stationloader.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.DimensionData;
import net.modificationstation.stationloader.api.common.event.level.LevelInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class MixinLevel {

    @Environment(EnvType.CLIENT)
    @Inject(method = "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;Lnet/minecraft/level/dimension/Dimension;J)V", at = @At("RETURN"))
    private void afterLevelInitialization(DimensionData dimensionData, String string, Dimension dimension, long seed, CallbackInfo ci) {
        afterLevelInitialization();
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "<init>(Lnet/minecraft/level/Level;Lnet/minecraft/level/dimension/Dimension;)V", at = @At("RETURN"))
    private void afterLevelInitialization(Level level, Dimension dimension, CallbackInfo ci) {
        afterLevelInitialization();
    }

    @Inject(method = "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/level/dimension/Dimension;)V", at = @At("RETURN"))
    private void afterLevelInitialization(DimensionData arg, String name, long l, Dimension dimension, CallbackInfo ci) {
        afterLevelInitialization();
    }

    private void afterLevelInitialization() {
        LevelInit.EVENT.getInvoker().onLevelInit((Level) (Object) this);
    }
}
