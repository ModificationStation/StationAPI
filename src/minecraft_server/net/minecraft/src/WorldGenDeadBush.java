// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            WorldGenerator, World, Block, BlockLeaves, 
//            BlockFlower

public class WorldGenDeadBush extends WorldGenerator
{

    public WorldGenDeadBush(int i)
    {
        field_28055_a = i;
    }

    public boolean generate(World world, Random random, int i, int j, int k)
    {
        for(int l = 0; ((l = world.getBlockId(i, j, k)) == 0 || l == Block.leaves.blockID) && j > 0; j--) { }
        for(int i1 = 0; i1 < 4; i1++)
        {
            int j1 = (i + random.nextInt(8)) - random.nextInt(8);
            int k1 = (j + random.nextInt(4)) - random.nextInt(4);
            int l1 = (k + random.nextInt(8)) - random.nextInt(8);
            if(world.isAirBlock(j1, k1, l1) && ((BlockFlower)Block.blocksList[field_28055_a]).canBlockStay(world, j1, k1, l1))
            {
                world.setBlock(j1, k1, l1, field_28055_a);
            }
        }

        return true;
    }

    private int field_28055_a;
}
