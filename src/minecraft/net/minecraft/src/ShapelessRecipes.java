// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            IRecipe, InventoryCrafting, ItemStack

public class ShapelessRecipes
    implements IRecipe
{

    public ShapelessRecipes(ItemStack itemstack, List list)
    {
        recipeOutput = itemstack;
        recipeItems = list;
    }

    public ItemStack getRecipeOutput()
    {
        return recipeOutput;
    }

    public boolean matches(InventoryCrafting inventorycrafting)
    {
        ArrayList arraylist = new ArrayList(recipeItems);
        int i = 0;
        do
        {
            if(i >= 3)
            {
                break;
            }
            for(int j = 0; j < 3; j++)
            {
                ItemStack itemstack = inventorycrafting.func_21103_b(j, i);
                if(itemstack == null)
                {
                    continue;
                }
                boolean flag = false;
                Iterator iterator = arraylist.iterator();
                do
                {
                    if(!iterator.hasNext())
                    {
                        break;
                    }
                    ItemStack itemstack1 = (ItemStack)iterator.next();
                    if(itemstack.itemID != itemstack1.itemID || itemstack1.getItemDamage() != -1 && itemstack.getItemDamage() != itemstack1.getItemDamage())
                    {
                        continue;
                    }
                    flag = true;
                    arraylist.remove(itemstack1);
                    break;
                } while(true);
                if(!flag)
                {
                    return false;
                }
            }

            i++;
        } while(true);
        return arraylist.isEmpty();
    }

    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting)
    {
        return recipeOutput.copy();
    }

    public int getRecipeSize()
    {
        return recipeItems.size();
    }

    private final ItemStack recipeOutput;
    private final List recipeItems;
}
