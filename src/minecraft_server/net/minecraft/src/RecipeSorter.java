// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Comparator;

// Referenced classes of package net.minecraft.src:
//            ShapelessRecipes, ShapedRecipes, IRecipe, CraftingManager

class RecipeSorter
    implements Comparator
{

    RecipeSorter(CraftingManager craftingmanager)
    {
//        super();
        craftingManager = craftingmanager;
    }

    public int compareRecipes(IRecipe irecipe, IRecipe irecipe1)
    {
        if((irecipe instanceof ShapelessRecipes) && (irecipe1 instanceof ShapedRecipes))
        {
            return 1;
        }
        if((irecipe1 instanceof ShapelessRecipes) && (irecipe instanceof ShapedRecipes))
        {
            return -1;
        }
        if(irecipe1.getRecipeSize() < irecipe.getRecipeSize())
        {
            return -1;
        }
        return irecipe1.getRecipeSize() <= irecipe.getRecipeSize() ? 0 : 1;
    }

    public int compare(Object obj, Object obj1)
    {
        return compareRecipes((IRecipe)obj, (IRecipe)obj1);
    }

    final CraftingManager craftingManager; /* synthetic field */
}
