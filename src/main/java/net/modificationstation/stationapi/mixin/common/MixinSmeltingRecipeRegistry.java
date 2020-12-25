package net.modificationstation.stationapi.mixin.common;

import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.modificationstation.stationapi.api.common.event.recipe.RecipeRegister;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.api.common.event.recipe.RecipeRegister.Vanilla.SMELTING;

@Mixin(SmeltingRecipeRegistry.class)
public class MixinSmeltingRecipeRegistry {

    @Mutable
    @Shadow
    @Final
    private static SmeltingRecipeRegistry INSTANCE;

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void afterRecipeRegister(CallbackInfo ci) {
        INSTANCE = (SmeltingRecipeRegistry) (Object) this;
        RecipeRegister.EVENT.getInvoker().registerRecipes(SMELTING.type());
        INSTANCE = null;
    }
}
