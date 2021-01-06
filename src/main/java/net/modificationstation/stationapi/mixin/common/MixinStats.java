package net.modificationstation.stationapi.mixin.common;

import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.common.event.recipe.BeforeRecipeStats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Stats.class)
public class MixinStats {

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "method_756()V", at = @At(value = "NEW", target = "()Ljava/util/HashSet;"))
    private static void beforeRecipeStats(CallbackInfo ci) {
        BeforeRecipeStats.EVENT.getInvoker().beforeRecipeStats();
    }
}
