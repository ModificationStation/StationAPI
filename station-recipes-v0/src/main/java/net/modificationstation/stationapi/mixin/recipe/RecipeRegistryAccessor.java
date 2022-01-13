package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.*;

@Mixin(RecipeRegistry.class)
public interface RecipeRegistryAccessor {

    @Invoker("addShapedRecipe")
    void stationapi$addShapedRecipe(ItemInstance itemInstance, Object... o);

    @Invoker("addShapelessRecipe")
    void stationapi$addShapelessRecipe(ItemInstance itemInstance, Object... o);

    @Accessor("recipes")
    List<Recipe> stationapi$getRecipes();
}
