// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Item, Block, ItemStack, CraftingManager

public class RecipesArmor
{

    public RecipesArmor()
    {
        recipeItems = (new Object[][] {
            new Object[] {
                Item.leather, Block.fire, Item.ingotIron, Item.diamond, Item.ingotGold
            }, new Object[] {
                Item.helmetLeather, Item.helmetChain, Item.helmetSteel, Item.helmetDiamond, Item.helmetGold
            }, new Object[] {
                Item.plateLeather, Item.plateChain, Item.plateSteel, Item.plateDiamond, Item.plateGold
            }, new Object[] {
                Item.legsLeather, Item.legsChain, Item.legsSteel, Item.legsDiamond, Item.legsGold
            }, new Object[] {
                Item.bootsLeather, Item.bootsChain, Item.bootsSteel, Item.bootsDiamond, Item.bootsGold
            }
        });
    }

    public void addRecipes(CraftingManager craftingmanager)
    {
        for(int i = 0; i < recipeItems[0].length; i++)
        {
            Object obj = recipeItems[0][i];
            for(int j = 0; j < recipeItems.length - 1; j++)
            {
                Item item = (Item)recipeItems[j + 1][i];
                craftingmanager.addRecipe(new ItemStack(item), new Object[] {
                    recipePatterns[j], Character.valueOf('X'), obj
                });
            }

        }

    }

    private String recipePatterns[][] = {
        {
            "XXX", "X X"
        }, {
            "X X", "XXX", "XXX"
        }, {
            "XXX", "X X", "X X"
        }, {
            "X X", "X X"
        }
    };
    private Object recipeItems[][];
}
