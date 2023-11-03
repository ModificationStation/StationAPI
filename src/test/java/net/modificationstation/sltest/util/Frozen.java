// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   Frozen.java

package net.modificationstation.sltest.util;


import net.minecraft.item.ItemStack;

public class Frozen
{

    public Frozen(ItemStack from, ItemStack to, int i)
    {
        frozenFrom = from;
        frozenTo = to;
        frozenPowerNeeded = i;
    }

    public ItemStack frozenFrom;
    public ItemStack frozenTo;
    public int frozenPowerNeeded;
}
