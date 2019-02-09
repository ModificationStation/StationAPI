// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            ItemStack, Block, BlockCloth, Item, 
//            CraftingManager

public class RecipesDyes
{

    public RecipesDyes()
    {
    }

    public void addRecipes(CraftingManager craftingmanager)
    {
        for(int i = 0; i < 16; i++)
        {
            craftingmanager.addShapelessRecipe(new ItemStack(Block.cloth, 1, BlockCloth.func_21035_d(i)), new Object[] {
                new ItemStack(Item.dyePowder, 1, i), new ItemStack(Item.itemsList[Block.cloth.blockID], 1, 0)
            });
        }

        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 11), new Object[] {
            Block.plantYellow
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 1), new Object[] {
            Block.plantRed
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 3, 15), new Object[] {
            Item.bone
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 9), new Object[] {
            new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 15)
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 14), new Object[] {
            new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 11)
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 10), new Object[] {
            new ItemStack(Item.dyePowder, 1, 2), new ItemStack(Item.dyePowder, 1, 15)
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 8), new Object[] {
            new ItemStack(Item.dyePowder, 1, 0), new ItemStack(Item.dyePowder, 1, 15)
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 7), new Object[] {
            new ItemStack(Item.dyePowder, 1, 8), new ItemStack(Item.dyePowder, 1, 15)
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 3, 7), new Object[] {
            new ItemStack(Item.dyePowder, 1, 0), new ItemStack(Item.dyePowder, 1, 15), new ItemStack(Item.dyePowder, 1, 15)
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 12), new Object[] {
            new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Item.dyePowder, 1, 15)
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 6), new Object[] {
            new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Item.dyePowder, 1, 2)
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 5), new Object[] {
            new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Item.dyePowder, 1, 1)
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 13), new Object[] {
            new ItemStack(Item.dyePowder, 1, 5), new ItemStack(Item.dyePowder, 1, 9)
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 3, 13), new Object[] {
            new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 9)
        });
        craftingmanager.addShapelessRecipe(new ItemStack(Item.dyePowder, 4, 13), new Object[] {
            new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 15)
        });
    }
}
