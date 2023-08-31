package net.modificationstation.stationapi.impl.vanillafix.recipe;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import lombok.val;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.tag.ItemTags;
import net.modificationstation.stationapi.api.tag.TagKey;

public class VanillaTagRecipeFixImpl {
    private static final Supplier<ImmutableMap<ItemBase, TagKey<ItemBase>>> TAGIFICATION_MAP = Suppliers.memoize(() -> {
        val builder = ImmutableMap.<ItemBase, TagKey<ItemBase>>builder();
        builder.put(BlockBase.WOOD.asItem(), ItemTags.PLANKS);
        return builder.build();
    });

    public static boolean tagifyIngredients(Object[] input) {
        boolean tagified = false;
        int i = 1;
        while (!(input[i++] instanceof Character));
        for (; i < input.length; i += 2) {
            Object rawIngredient = input[i];

            ItemBase ingredient;
            if (rawIngredient instanceof ItemBase item) ingredient = item;
            else if (rawIngredient instanceof BlockBase block) ingredient = block.asItem();
            else continue;

            val tagificationMap = TAGIFICATION_MAP.get();
            if (!tagificationMap.containsKey(ingredient)) continue;

            tagified = true;
            input[i] = tagificationMap.get(ingredient);
        }
        return tagified;
    }
}
