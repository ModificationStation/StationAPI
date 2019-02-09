// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            WorldGenerator, Block, World, Material, 
//            MathHelper

public class WorldGenClay extends WorldGenerator
{

    public WorldGenClay(int i)
    {
        clayBlockId = Block.blockClay.blockID;
        numberOfBlocks = i;
    }

    public boolean generate(World world, Random random, int i, int j, int k)
    {
        if(world.getBlockMaterial(i, j, k) != Material.water)
        {
            return false;
        }
        float f = random.nextFloat() * 3.141593F;
        double d = (float)(i + 8) + (MathHelper.sin(f) * (float)numberOfBlocks) / 8F;
        double d1 = (float)(i + 8) - (MathHelper.sin(f) * (float)numberOfBlocks) / 8F;
        double d2 = (float)(k + 8) + (MathHelper.cos(f) * (float)numberOfBlocks) / 8F;
        double d3 = (float)(k + 8) - (MathHelper.cos(f) * (float)numberOfBlocks) / 8F;
        double d4 = j + random.nextInt(3) + 2;
        double d5 = j + random.nextInt(3) + 2;
        for(int l = 0; l <= numberOfBlocks; l++)
        {
            double d6 = d + ((d1 - d) * (double)l) / (double)numberOfBlocks;
            double d7 = d4 + ((d5 - d4) * (double)l) / (double)numberOfBlocks;
            double d8 = d2 + ((d3 - d2) * (double)l) / (double)numberOfBlocks;
            double d9 = (random.nextDouble() * (double)numberOfBlocks) / 16D;
            double d10 = (double)(MathHelper.sin(((float)l * 3.141593F) / (float)numberOfBlocks) + 1.0F) * d9 + 1.0D;
            double d11 = (double)(MathHelper.sin(((float)l * 3.141593F) / (float)numberOfBlocks) + 1.0F) * d9 + 1.0D;
            int i1 = MathHelper.floor_double(d6 - d10 / 2D);
            int j1 = MathHelper.floor_double(d6 + d10 / 2D);
            int k1 = MathHelper.floor_double(d7 - d11 / 2D);
            int l1 = MathHelper.floor_double(d7 + d11 / 2D);
            int i2 = MathHelper.floor_double(d8 - d10 / 2D);
            int j2 = MathHelper.floor_double(d8 + d10 / 2D);
            for(int k2 = i1; k2 <= j1; k2++)
            {
                for(int l2 = k1; l2 <= l1; l2++)
                {
                    for(int i3 = i2; i3 <= j2; i3++)
                    {
                        double d12 = (((double)k2 + 0.5D) - d6) / (d10 / 2D);
                        double d13 = (((double)l2 + 0.5D) - d7) / (d11 / 2D);
                        double d14 = (((double)i3 + 0.5D) - d8) / (d10 / 2D);
                        if(d12 * d12 + d13 * d13 + d14 * d14 >= 1.0D)
                        {
                            continue;
                        }
                        int j3 = world.getBlockId(k2, l2, i3);
                        if(j3 == Block.sand.blockID)
                        {
                            world.setBlock(k2, l2, i3, clayBlockId);
                        }
                    }

                }

            }

        }

        return true;
    }

    private int clayBlockId;
    private int numberOfBlocks;
}
