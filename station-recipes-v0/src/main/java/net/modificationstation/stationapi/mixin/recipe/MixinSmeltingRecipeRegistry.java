package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.SMELTING;

@Mixin(SmeltingRecipeRegistry.class)
public class MixinSmeltingRecipeRegistry {

    @Mutable
    @Shadow
    @Final
    private static SmeltingRecipeRegistry INSTANCE;

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void afterRecipeRegister(CallbackInfo ci) {
        INSTANCE = (SmeltingRecipeRegistry) (Object) this;
        StationAPI.EVENT_BUS.post(new RecipeRegisterEvent(SMELTING.type()));
    }
}
