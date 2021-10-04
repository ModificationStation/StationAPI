package net.modificationstation.stationapi.mixin.dimension;

import net.minecraft.level.dimension.Dimension;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.*;

@Mixin(Dimension.class)
public class MixinDimension {

    @Shadow public int id;

    @Inject(
            method = "getByID(I)Lnet/minecraft/level/dimension/Dimension;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getDimension(int id, CallbackInfoReturnable<Dimension> cir) {
        cir.setReturnValue(DimensionRegistry.INSTANCE.get(id).map(Supplier::get).orElse(null));
    }
}
