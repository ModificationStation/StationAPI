package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.StatRegistryEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Stats.class)
class StatsMixin {
    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/stat/Stats;FISH_CAUGHT:Lnet/minecraft/stat/Stat;",
                    opcode = Opcodes.PUTSTATIC,
                    shift = At.Shift.AFTER
            )
    )
    private static void stationapi_afterStatRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new StatRegistryEvent());
    }
}
