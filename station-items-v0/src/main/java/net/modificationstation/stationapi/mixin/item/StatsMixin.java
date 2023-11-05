package net.modificationstation.stationapi.mixin.item;

import net.minecraft.item.Item;
import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Stats.class)
class StatsMixin {
    @ModifyConstant(
            method = "initializeCraftedItemStats",
            constant = @Constant(intValue = 32000)
    )
    private static int stationapi_getItemsSize(int constant) {
        return Item.ITEMS.length;
    }

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
