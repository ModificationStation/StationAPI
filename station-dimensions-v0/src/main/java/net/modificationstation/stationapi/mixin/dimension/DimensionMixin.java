package net.modificationstation.stationapi.mixin.dimension;

import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Dimension.class)
class DimensionMixin {
    @Shadow public int id;

    @Inject(
            method = "method_1767",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void stationapi_getDimension(int id, CallbackInfoReturnable<Dimension> cir) {
        cir.setReturnValue(DimensionRegistry.INSTANCE.getByLegacyId(id).map(dimensionFactory -> dimensionFactory.factory.get()).orElse(null));
    }
}
