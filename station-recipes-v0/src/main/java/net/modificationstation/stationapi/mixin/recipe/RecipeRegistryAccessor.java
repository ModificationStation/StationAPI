package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeRegistry.class)
public interface RecipeRegistryAccessor {

    @Invoker("addShapedRecipe")
    void stationapi$addShapedRecipe(ItemInstance itemInstance, Object... o);

    @Invoker("addShapelessRecipe")
    void stationapi$addShapelessRecipe(ItemInstance itemInstance, Object... o);
}
