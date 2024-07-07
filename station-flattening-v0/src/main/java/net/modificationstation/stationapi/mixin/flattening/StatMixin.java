package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.StatRegistry;
import net.modificationstation.stationapi.api.stat.StationFlatteningStat;
import net.modificationstation.stationapi.impl.stat.VanillaIdHolder;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Stat.class)
class StatMixin implements StationFlatteningStat, VanillaIdHolder {
    @Mutable
    @Shadow @Final public int id;
    @Unique
    private RegistryEntry.Reference<Stat> stationapi_registryEntry;
    @Unique
    private int stationapi_vanillaId = -1;

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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    @Unique
    public void setRawId(int rawId) {
        id = rawId;
    }

    @Override
    public void setVanillaId(int vanillaId) {
        stationapi_vanillaId = vanillaId;
    }

    @WrapOperation(
            method = "addStat",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/stat/Stat;id:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 4
            )
    )
    private int stationapi_useVanillaId(Stat instance, Operation<Integer> original) {
        final int vanillaId = ((StatMixin) (Object) instance).stationapi_vanillaId;
        return vanillaId == -1 ? original.call(instance) : vanillaId;
    }
}
