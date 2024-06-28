package net.modificationstation.stationapi.mixin.item;

import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Stats.class)
class StatsMixin {
    @Inject(
            method = "initializeCraftedItemStats",
            at = @At(
                    value = "NEW",
                    target = "()Ljava/util/HashSet;",
                    shift = At.Shift.BEFORE
            )
    )
    private static void stationapi_afterBlockAndItemRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(AfterBlockAndItemRegisterEvent.builder().build());
    }
}
