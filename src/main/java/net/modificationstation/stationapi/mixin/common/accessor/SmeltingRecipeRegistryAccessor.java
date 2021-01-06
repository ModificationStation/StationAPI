package net.modificationstation.stationapi.mixin.common.accessor;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(SmeltingRecipeRegistry.class)
public interface SmeltingRecipeRegistryAccessor {

    @SuppressWarnings("UnusedReturnValue")
    @Invoker("<init>")
    static SmeltingRecipeRegistry invokeCor() {
        throw new UnsupportedOperationException("Mixin!");
    }

    @Accessor
    Map<Object, ItemInstance> getRecipes();
}
