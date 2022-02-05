package net.modificationstation.sltest.recipe;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.sltest.block.Blocks;
import net.modificationstation.sltest.item.ItemListener;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;

import static net.modificationstation.sltest.SLTest.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public class RecipeListener {

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        RecipeRegisterEvent.Vanilla type = RecipeRegisterEvent.Vanilla.fromType(event.recipeId);
        switch (type) {
            case CRAFTING_SHAPELESS -> {
                CraftingRegistry.addShapelessRecipe(new ItemInstance(ItemListener.testItem), new ItemInstance(BlockBase.DIRT));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(Blocks.TEST_BLOCK.get()), new ItemInstance(BlockBase.SAND));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(ItemBase.flintAndSteel), new ItemInstance(Blocks.TEST_BLOCK.get()));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.REDSTONE_DUST), new ItemInstance(ItemBase.flintAndSteel));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.RAIL), new ItemInstance(BlockBase.REDSTONE_DUST));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.GOLDEN_RAIL), new ItemInstance(BlockBase.RAIL));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.REDSTONE_REPEATER), new ItemInstance(BlockBase.GOLDEN_RAIL));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.PISTON), new ItemInstance(BlockBase.REDSTONE_REPEATER));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.FLOWING_WATER), new ItemInstance(BlockBase.PISTON));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(Blocks.TEST_ANIMATED_BLOCK.get()), new ItemInstance(BlockBase.FLOWING_WATER));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.OBSIDIAN), new ItemInstance(Blocks.TEST_ANIMATED_BLOCK.get()));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(Blocks.TEST_ANIMATED_BLOCK.get(), 1, 1), new ItemInstance(BlockBase.OBSIDIAN));
                CraftingRegistry.addShapelessRecipe(new ItemInstance(ItemBase.dyePowder, 1, 5), new ItemInstance(Blocks.TEST_ANIMATED_BLOCK.get(), 1, 1));
            }
            case CRAFTING_SHAPED -> CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.testPickaxe), "X X", " Y ", " Y ", 'X', Blocks.TEST_ANIMATED_BLOCK.get(), 'Y', BlockBase.OBSIDIAN);
            case SMELTING -> {
                SmeltingRegistry.addSmeltingRecipe(new ItemInstance(BlockBase.OBSIDIAN), new ItemInstance(BlockBase.COAL_ORE, 2));
                SmeltingRegistry.addSmeltingRecipe(new ItemInstance(Blocks.TEST_ANIMATED_BLOCK.get(), 1, 0), new ItemInstance(Blocks.TEST_ANIMATED_BLOCK.get(), 1, 1));
                ItemInstance itemInstance = new ItemInstance(ItemListener.testNBTItem);
                StationNBT.cast(itemInstance).getStationNBT().put(of(MODID, "rand_num").toString(), 10);
                SmeltingRegistry.addSmeltingRecipe(ItemListener.testItem.id, itemInstance);
            }
        }
    }
}
