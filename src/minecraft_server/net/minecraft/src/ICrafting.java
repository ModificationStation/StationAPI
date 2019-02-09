// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;

// Referenced classes of package net.minecraft.src:
//            Container, ItemStack

public interface ICrafting
{

    public abstract void updateCraftingInventory(Container container, List list);

    public abstract void updateCraftingInventorySlot(Container container, int i, ItemStack itemstack);

    public abstract void updateCraftingInventoryInfo(Container container, int i, int j);
}
