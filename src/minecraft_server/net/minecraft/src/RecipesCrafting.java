// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            ItemStack, Block, CraftingManager

public class RecipesCrafting
{

    public RecipesCrafting()
    {
    }

    public void addRecipes(CraftingManager craftingmanager)
    {
        craftingmanager.addRecipe(new ItemStack(Block.chest), new Object[] {
            "###", "# #", "###", Character.valueOf('#'), Block.planks
        });
        craftingmanager.addRecipe(new ItemStack(Block.stoneOvenIdle), new Object[] {
            "###", "# #", "###", Character.valueOf('#'), Block.cobblestone
        });
        craftingmanager.addRecipe(new ItemStack(Block.workbench), new Object[] {
            "##", "##", Character.valueOf('#'), Block.planks
        });
        craftingmanager.addRecipe(new ItemStack(Block.sandStone), new Object[] {
            "##", "##", Character.valueOf('#'), Block.sand
        });
    }
}
