package net.modificationstation.stationapi.mixin.common;

import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.common.event.OreDictRegister;
import net.modificationstation.stationapi.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationapi.api.common.util.OreDict;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static net.modificationstation.stationapi.api.common.event.recipe.RecipeRegister.Vanilla.CRAFTING_SHAPED;
import static net.modificationstation.stationapi.api.common.event.recipe.RecipeRegister.Vanilla.CRAFTING_SHAPELESS;

@Mixin(RecipeRegistry.class)
public class MixinRecipeRegistry {

    @Mutable
    @Shadow
    @Final
    private static RecipeRegistry INSTANCE;

    @Shadow private List recipes;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V"))
    private <T> void afterRecipeRegister(List<T> list, Comparator<? super T> c) {
        INSTANCE = (RecipeRegistry) (Object) this;
        OreDictRegister.EVENT.getInvoker().registerOreDict(OreDict.INSTANCE);
        RecipeRegister.EVENT.getInvoker().registerRecipes(CRAFTING_SHAPED.type());
        RecipeRegister.EVENT.getInvoker().registerRecipes(CRAFTING_SHAPELESS.type());
        Collections.sort(list, c);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void restoreInstance(CallbackInfo ci) {
        INSTANCE = null;
    }
}
