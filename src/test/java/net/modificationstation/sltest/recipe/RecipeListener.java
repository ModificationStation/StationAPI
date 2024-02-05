package net.modificationstation.sltest.recipe;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.sltest.block.Blocks;
import net.modificationstation.sltest.item.ItemListener;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.item.StationItemNbt;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;

import static net.modificationstation.sltest.SLTest.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

public class RecipeListener {

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        RecipeRegisterEvent.Vanilla type = RecipeRegisterEvent.Vanilla.fromType(event.recipeId);
        switch (type) {
            case CRAFTING_SHAPELESS -> {
                CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.testItem), new ItemStack(Block.DIRT));
                CraftingRegistry.addShapelessRecipe(new ItemStack(Blocks.TEST_BLOCK.get()), new ItemStack(Block.SAND));
                CraftingRegistry.addShapelessRecipe(new ItemStack(Item.FLINT_AND_STEEL), new ItemStack(Blocks.TEST_BLOCK.get()));
                CraftingRegistry.addShapelessRecipe(new ItemStack(Block.REDSTONE_WIRE), new ItemStack(Item.FLINT_AND_STEEL));
                CraftingRegistry.addShapelessRecipe(new ItemStack(Block.RAIL), new ItemStack(Block.REDSTONE_WIRE));
                CraftingRegistry.addShapelessRecipe(new ItemStack(Block.POWERED_RAIL), new ItemStack(Block.RAIL));
                CraftingRegistry.addShapelessRecipe(new ItemStack(Block.PISTON), new ItemStack(Block.POWERED_RAIL));
                CraftingRegistry.addShapelessRecipe(new ItemStack(Blocks.TEST_ANIMATED_BLOCK.get()), new ItemStack(Block.PISTON));
                CraftingRegistry.addShapelessRecipe(new ItemStack(Block.OBSIDIAN), new ItemStack(Blocks.TEST_ANIMATED_BLOCK.get()));
                CraftingRegistry.addShapelessRecipe(new ItemStack(Blocks.TEST_ANIMATED_BLOCK.get(), 1, 1), new ItemStack(Block.OBSIDIAN));
//                CraftingRegistry.addShapelessRecipe(new ItemInstance(ItemBase.dyePowder, 1, 5), new ItemInstance(Blocks.TEST_ANIMATED_BLOCK.get(), 1, 1));
                CraftingRegistry.addShapelessRecipe(new ItemStack(Item.SLIMEBALL, 64), new ItemStack(Blocks.TEST_ANIMATED_BLOCK.get(), 1, 1));
            }
            case CRAFTING_SHAPED -> CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.testPickaxe), "X X", " Y ", " Y ", 'X', Blocks.TEST_ANIMATED_BLOCK.get(), 'Y', Block.OBSIDIAN);
            case SMELTING -> {
                SmeltingRegistry.addSmeltingRecipe(new ItemStack(Block.OBSIDIAN), new ItemStack(Block.COAL_ORE, 2));
                SmeltingRegistry.addSmeltingRecipe(new ItemStack(Blocks.TEST_ANIMATED_BLOCK.get(), 1, 0), new ItemStack(Blocks.TEST_ANIMATED_BLOCK.get(), 1, 1));
                ItemStack itemInstance = new ItemStack(ItemListener.testNBTItem);
                StationItemNbt.class.cast(itemInstance).getStationNbt().putInt(of(NAMESPACE, "rand_num").toString(), 10);
                SmeltingRegistry.addSmeltingRecipe(ItemListener.testItem.id, itemInstance);
            }
        }
    }
}
