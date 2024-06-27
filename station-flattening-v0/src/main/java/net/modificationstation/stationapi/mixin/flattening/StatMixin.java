package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.StatRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Stat.class)
class StatMixin {
    @Unique
    private RegistryEntry.Reference<Stat> stationapi_registryEntry;

    @ModifyVariable(
            method = "<init>(ILjava/lang/String;Lnet/minecraft/stat/StatFormatter;)V",
            index = 1,
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=0",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            argsOnly = true
    )
    private int stationapi_ensureCapacity(int rawId) {
        return (stationapi_registryEntry = StatRegistry.INSTANCE.createReservedEntry(rawId, (Stat) (Object) this)).reservedRawId();
    }
}
