package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED;
import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS;

@Mixin(RecipeRegistry.class)
public class MixinRecipeRegistry {

    @Mutable
    @Shadow
    @Final
    private static RecipeRegistry INSTANCE;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V"))
    private <T> void afterRecipeRegister(List<T> list, Comparator<? super T> c) {
        if (INSTANCE == null)
            INSTANCE = (RecipeRegistry) (Object) this;
        StationAPI.EVENT_BUS.post(RecipeRegisterEvent.builder().recipeId(CRAFTING_SHAPED.type()).build());
        StationAPI.EVENT_BUS.post(RecipeRegisterEvent.builder().recipeId(CRAFTING_SHAPELESS.type()).build());
        //noinspection Java8ListSort
        Collections.sort(list, c);
    }
}
