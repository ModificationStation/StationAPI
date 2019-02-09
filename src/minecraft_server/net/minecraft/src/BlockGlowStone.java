// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Block, Item, Material

public class BlockGlowStone extends Block
{

    public BlockGlowStone(int i, int j, Material material)
    {
        super(i, j, material);
    }

    public int quantityDropped(Random random)
    {
        return 2 + random.nextInt(3);
    }

    public int idDropped(int i, Random random)
    {
        return Item.lightStoneDust.shiftedIndex;
    }
}
