package net.modificationstation.sltest.recipe;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.sltest.block.BlockListener;
import net.modificationstation.sltest.item.ItemListener;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.common.item.HasItemEntity;
import net.modificationstation.stationapi.api.common.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.common.recipe.SmeltingRegistry;

public class RecipeListener {

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        RecipeRegisterEvent.Vanilla type = RecipeRegisterEvent.Vanilla.fromType(event.recipeId);
        switch (type) {
            case CRAFTING_SHAPELESS: {
                CraftingRegistry.addShapelessRecipe(new ItemInstance(ItemListener.testItem), new ItemInstance(BlockBase.DIRT));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockListener.testBlock), new ItemInstance(BlockBase.SAND));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(ItemBase.flintAndSteel), new ItemInstance(BlockListener.testBlock));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.REDSTONE_DUST), new ItemInstance(ItemBase.flintAndSteel));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.RAIL), new ItemInstance(BlockBase.REDSTONE_DUST));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.GOLDEN_RAIL), new ItemInstance(BlockBase.RAIL));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.REDSTONE_REPEATER), new ItemInstance(BlockBase.GOLDEN_RAIL));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.PISTON), new ItemInstance(BlockBase.REDSTONE_REPEATER));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.FLOWING_WATER), new ItemInstance(BlockBase.PISTON));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockListener.testAnimatedBlock), new ItemInstance(BlockBase.FLOWING_WATER));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.OBSIDIAN), new ItemInstance(BlockListener.testAnimatedBlock));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockListener.testAnimatedBlock, 1, 1), new ItemInstance(BlockBase.OBSIDIAN));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(ItemBase.dyePowder, 1, 5), new ItemInstance(BlockListener.testAnimatedBlock, 1, 1));
                break;
            }
            case CRAFTING_SHAPED: {
                CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.testPickaxe), "X X", " Y ", " Y ", 'X', BlockListener.testAnimatedBlock, 'Y', BlockBase.OBSIDIAN);
                break;
            }
            case SMELTING: {
                SmeltingRegistry.addSmeltingRecipe(new ItemInstance(BlockBase.OBSIDIAN), new ItemInstance(BlockBase.COAL_ORE, 2));
                SmeltingRegistry.addSmeltingRecipe(new ItemInstance(BlockListener.testAnimatedBlock, 1, 0), new ItemInstance(BlockListener.testAnimatedBlock, 1, 1));
                ItemInstance itemInstance = new ItemInstance(ItemListener.testNBTItem);
                HasItemEntity.cast(itemInstance).setItemEntity(null);
                SmeltingRegistry.addSmeltingRecipe(ItemListener.testItem.id, itemInstance);
                break;
            }
        }
    }
}
