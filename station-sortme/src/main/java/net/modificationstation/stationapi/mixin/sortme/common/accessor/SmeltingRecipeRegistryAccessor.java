package net.modificationstation.stationapi.mixin.sortme.common.accessor;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.*;

@Mixin(SmeltingRecipeRegistry.class)
public interface SmeltingRecipeRegistryAccessor {

    @SuppressWarnings("UnusedReturnValue")
    @Invoker("<init>")
    static SmeltingRecipeRegistry invokeCor() {
        throw new UnsupportedOperationException("Mixin!");
    }
}
