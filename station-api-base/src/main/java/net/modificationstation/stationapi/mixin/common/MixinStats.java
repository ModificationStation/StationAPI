package net.modificationstation.stationapi.mixin.common;

import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.event.recipe.BeforeRecipeStatsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Stats.class)
public class MixinStats {

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "setupCrafting()V", at = @At(value = "NEW", target = "()Ljava/util/HashSet;"))
    private static void beforeRecipeStats(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new BeforeRecipeStatsEvent());
    }
}
