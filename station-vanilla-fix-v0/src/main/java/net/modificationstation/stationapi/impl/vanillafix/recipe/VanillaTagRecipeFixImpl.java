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
        builder.put(ItemBase.stick, ItemTags.WOODEN_STICKS);
        builder.put(BlockBase.STONE.asItem(), ItemTags.STONE);
        builder.put(BlockBase.COBBLESTONE.asItem(), ItemTags.COBBLESTONE);
        builder.put(ItemBase.ironIngot, ItemTags.IRON_INGOT);
        builder.put(ItemBase.goldIngot, ItemTags.GOLD_INGOT);
        builder.put(ItemBase.diamond, ItemTags.DIAMOND);
        builder.put(ItemBase.string, ItemTags.STRINGS);
        builder.put(ItemBase.leather, ItemTags.LEATHERS);
        builder.put(ItemBase.redstoneDust, ItemTags.REDSTONE);
        builder.put(BlockBase.SAND.asItem(), ItemTags.SAND);
        builder.put(ItemBase.egg, ItemTags.EGGS);
        builder.put(BlockBase.STONE_PRESSURE_PLATE.asItem(), ItemTags.STONE_PRESSURE_PLATES);
        builder.put(BlockBase.WOOL.asItem(), ItemTags.WOOL);
        builder.put(BlockBase.IRON_BLOCK.asItem(), ItemTags.IRON_STORAGE_BLOCK);
        builder.put(BlockBase.GOLD_BLOCK.asItem(), ItemTags.GOLD_STORAGE_BLOCK);
        builder.put(BlockBase.LAPIS_LAZULI_BLOCK.asItem(), ItemTags.LAPIS_STORAGE_BLOCK);
        builder.put(BlockBase.DIAMOND_BLOCK.asItem(), ItemTags.DIAMOND_STORAGE_BLOCK);
        builder.put(ItemBase.compass, ItemTags.COMPASSES);
        builder.put(ItemBase.glowstoneDust, ItemTags.GLOWSTONE);
        builder.put(ItemBase.flint, ItemTags.FLINT);
        builder.put(ItemBase.feather, ItemTags.FEATHERS);
        builder.put(BlockBase.CHEST.asItem(), ItemTags.CHESTS);
        builder.put(BlockBase.FURNACE.asItem(), ItemTags.FURNACE);
        builder.put(ItemBase.bone, ItemTags.BONES);
        builder.put(BlockBase.CACTUS.asItem(), ItemTags.CACTUS);
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
