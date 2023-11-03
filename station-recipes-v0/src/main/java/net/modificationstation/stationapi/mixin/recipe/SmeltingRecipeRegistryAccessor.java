package net.modificationstation.stationapi.mixin.recipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeManager;

@Mixin(SmeltingRecipeManager.class)
public interface SmeltingRecipeRegistryAccessor {

    @Accessor
    Map<Object, ItemStack> getRecipes();
}
