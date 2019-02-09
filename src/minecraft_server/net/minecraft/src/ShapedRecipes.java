// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            IRecipe, ItemStack, InventoryCrafting

public class ShapedRecipes
    implements IRecipe
{

    public ShapedRecipes(int i, int j, ItemStack aitemstack[], ItemStack itemstack)
    {
        field_21141_a = itemstack.itemID;
        field_21140_b = i;
        field_21144_c = j;
        field_21143_d = aitemstack;
        field_21142_e = itemstack;
    }

    public ItemStack func_25077_b()
    {
        return field_21142_e;
    }

    public boolean func_21134_a(InventoryCrafting inventorycrafting)
    {
        for(int i = 0; i <= 3 - field_21140_b; i++)
        {
            for(int j = 0; j <= 3 - field_21144_c; j++)
            {
                if(func_21139_a(inventorycrafting, i, j, true))
                {
                    return true;
                }
                if(func_21139_a(inventorycrafting, i, j, false))
                {
                    return true;
                }
            }

        }

        return false;
    }

    private boolean func_21139_a(InventoryCrafting inventorycrafting, int i, int j, boolean flag)
    {
        for(int k = 0; k < 3; k++)
        {
            for(int l = 0; l < 3; l++)
            {
                int i1 = k - i;
                int j1 = l - j;
                ItemStack itemstack = null;
                if(i1 >= 0 && j1 >= 0 && i1 < field_21140_b && j1 < field_21144_c)
                {
                    if(flag)
                    {
                        itemstack = field_21143_d[(field_21140_b - i1 - 1) + j1 * field_21140_b];
                    } else
                    {
                        itemstack = field_21143_d[i1 + j1 * field_21140_b];
                    }
                }
                ItemStack itemstack1 = inventorycrafting.func_21084_a(k, l);
                if(itemstack1 == null && itemstack == null)
                {
                    continue;
                }
                if(itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null)
                {
                    return false;
                }
                if(itemstack.itemID != itemstack1.itemID)
                {
                    return false;
                }
                if(itemstack.getItemDamage() != -1 && itemstack.getItemDamage() != itemstack1.getItemDamage())
                {
                    return false;
                }
            }

        }

        return true;
    }

    public ItemStack func_21136_b(InventoryCrafting inventorycrafting)
    {
        return new ItemStack(field_21142_e.itemID, field_21142_e.stackSize, field_21142_e.getItemDamage());
    }

    public int getRecipeSize()
    {
        return field_21140_b * field_21144_c;
    }

    private int field_21140_b;
    private int field_21144_c;
    private ItemStack field_21143_d[];
    private ItemStack field_21142_e;
    public final int field_21141_a;
}
