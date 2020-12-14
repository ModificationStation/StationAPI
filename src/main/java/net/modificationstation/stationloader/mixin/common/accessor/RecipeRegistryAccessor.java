package net.modificationstation.stationloader.mixin.common.accessor;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeRegistry.class)
public interface RecipeRegistryAccessor {

    @Invoker
    void invokeAddShapedRecipe(ItemInstance itemInstance, Object... o);

    @Invoker
    void invokeAddShapelessRecipe(ItemInstance itemInstance, Object... o);
}
