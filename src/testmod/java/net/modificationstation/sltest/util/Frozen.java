// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   Frozen.java

package net.modificationstation.sltest.util;


// Referenced classes of package net.minecraft.src:
//            ItemStack

import net.minecraft.item.ItemInstance;

public class Frozen
{

    public Frozen(ItemInstance from, ItemInstance to, int i)
    {
        frozenFrom = from;
        frozenTo = to;
        frozenPowerNeeded = i;
    }

    public ItemInstance frozenFrom;
    public ItemInstance frozenTo;
    public int frozenPowerNeeded;
}
