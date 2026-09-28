package net.modificationstation.stationapi.mixin.recipe;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.CraftingRecipeManager;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.CraftingResultEvent;
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

@Mixin(CraftingRecipeManager.class)
class CraftingRecipeManagerMixin {
    @Mutable
    @Shadow
    @Final
    private static CraftingRecipeManager INSTANCE;

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
            INSTANCE = (CraftingRecipeManager) (Object) this;
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

    @WrapOperation(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/CraftingRecipe;craft(Lnet/minecraft/inventory/CraftingInventory;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack stationapi_redirectOutputEvent(CraftingRecipe instance, CraftingInventory craftingInventory, Operation<ItemStack> original) {
        ItemStack output = original.call(instance, craftingInventory);
        CraftingResultEvent event = CraftingResultEvent.builder().grid(craftingInventory).itemCrafted(output).build();
        StationAPI.EVENT_BUS.post(event);
        return event.itemCrafted;
    }
}
