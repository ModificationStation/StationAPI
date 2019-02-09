// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

// Referenced classes of package net.minecraft.src:
//            Block, ItemStack, Item

public class FurnaceRecipes
{

    public static final FurnaceRecipes smelting()
    {
        return smeltingBase;
    }

    private FurnaceRecipes()
    {
        smeltingList = new HashMap();
        addSmelting(Block.oreIron.blockID, new ItemStack(Item.ingotIron));
        addSmelting(Block.oreGold.blockID, new ItemStack(Item.ingotGold));
        addSmelting(Block.oreDiamond.blockID, new ItemStack(Item.diamond));
        addSmelting(Block.sand.blockID, new ItemStack(Block.glass));
        addSmelting(Item.porkRaw.shiftedIndex, new ItemStack(Item.porkCooked));
        addSmelting(Item.fishRaw.shiftedIndex, new ItemStack(Item.fishCooked));
        addSmelting(Block.cobblestone.blockID, new ItemStack(Block.stone));
        addSmelting(Item.clay.shiftedIndex, new ItemStack(Item.brick));
        addSmelting(Block.cactus.blockID, new ItemStack(Item.dyePowder, 1, 2));
        addSmelting(Block.wood.blockID, new ItemStack(Item.coal, 1, 1));
    }

    public void addSmelting(int i, ItemStack itemstack)
    {
        smeltingList.put(Integer.valueOf(i), itemstack);
    }

    public ItemStack getSmeltingResult(int i)
    {
        return (ItemStack)smeltingList.get(Integer.valueOf(i));
    }

    public Map getSmeltingList()
    {
        return smeltingList;
    }

    private static final FurnaceRecipes smeltingBase = new FurnaceRecipes();
    private Map smeltingList;

}
