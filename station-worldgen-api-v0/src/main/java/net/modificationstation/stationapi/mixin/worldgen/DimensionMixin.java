package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Dimension.class)
public class DimensionMixin {
    @Shadow
    public World world;

    @Inject(method = "setWorld", at = @At(value = "RETURN"))
    private void modifyBiomes(CallbackInfo ci) {
        BiomeAPI.modifyBiomes(world);
    }
}
