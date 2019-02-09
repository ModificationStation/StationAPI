// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            InventoryCrafting, ItemStack

public interface IRecipe
{

    public abstract boolean func_21134_a(InventoryCrafting inventorycrafting);

    public abstract ItemStack func_21136_b(InventoryCrafting inventorycrafting);

    public abstract int getRecipeSize();

    public abstract ItemStack func_25077_b();
}
