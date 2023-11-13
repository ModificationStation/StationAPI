package net.modificationstation.stationapi.mixin.worldgen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionData;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
class WorldMixin {
    @Inject(
        method = "<init>(Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/dimension/Dimension;method_1768(Lnet/minecraft/world/World;)V",
            shift = Shift.BEFORE
        )
    )
    private void stationapi_onInit(DimensionData data, String name, long seed, Dimension dimension, CallbackInfo info) {
        if (data.method_1737() != null) {
            seed = data.method_1737().getSeed();
        }
        BiomeAPI.init(World.class.cast(this), seed);
    }
    
    @Inject(
        method = "<init>(Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;Lnet/minecraft/world/dimension/Dimension;J)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/dimension/Dimension;method_1768(Lnet/minecraft/world/World;)V",
            shift = Shift.BEFORE
        )
    )
    @Environment(value= EnvType.CLIENT)
    private void stationapi_onInit(DimensionData data, String name, Dimension dimension, long seed, CallbackInfo info) {
        if (data.method_1737() != null) {
            seed = data.method_1737().getSeed();
        }
        BiomeAPI.init(World.class.cast(this), seed);
    }
    
    @Inject(
        method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/dimension/Dimension;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/dimension/Dimension;method_1768(Lnet/minecraft/world/World;)V",
            shift = Shift.BEFORE
        )
    )
    @Environment(value= EnvType.CLIENT)
    private void stationapi_onInit(World world, Dimension dimension, CallbackInfo info) {
        BiomeAPI.init(world, world.getSeed());
    }
}
