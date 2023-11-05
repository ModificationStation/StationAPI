package net.modificationstation.stationapi.impl.vanillafix.recipe;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.registry.tag.ItemTags;
import net.modificationstation.stationapi.api.tag.TagKey;

public class VanillaTagRecipeFixImpl {
    private static final Supplier<ImmutableMap<Item, TagKey<Item>>> TAGIFICATION_MAP = Suppliers.memoize(() -> {
        val builder = ImmutableMap.<Item, TagKey<Item>>builder();
        builder.put(Block.PLANKS.asItem(), ItemTags.PLANKS);
        return builder.build();
    });

    public static boolean tagifyIngredients(Object[] input) {
        boolean tagified = false;
        int i = 1;
        while (!(input[i++] instanceof Character));
        for (; i < input.length; i += 2) {
            Object rawIngredient = input[i];

            Item ingredient;
            if (rawIngredient instanceof Item item) ingredient = item;
            else if (rawIngredient instanceof Block block) ingredient = block.asItem();
            else continue;

            val tagificationMap = TAGIFICATION_MAP.get();
            if (!tagificationMap.containsKey(ingredient)) continue;

            tagified = true;
            input[i] = tagificationMap.get(ingredient);
        }
        return tagified;
    }
}
