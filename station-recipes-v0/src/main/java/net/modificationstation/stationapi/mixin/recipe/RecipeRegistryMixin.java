package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED;
import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS;

@Mixin(RecipeRegistry.class)
class RecipeRegistryMixin {
    @Mutable
    @Shadow
    @Final
    private static RecipeRegistry INSTANCE;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/ArrayList;<init>()V"
            )
    )
    private void stationapi_setInstanceEarly(CallbackInfo ci) {
        if (INSTANCE == null)
            //noinspection DataFlowIssue
            INSTANCE = (RecipeRegistry) (Object) this;
    }

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V"
            )
    )
    private void stationapi_postRecipeEvents(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(RecipeRegisterEvent.builder().recipeId(CRAFTING_SHAPED.type()).build());
        StationAPI.EVENT_BUS.post(RecipeRegisterEvent.builder().recipeId(CRAFTING_SHAPELESS.type()).build());
    }
}
