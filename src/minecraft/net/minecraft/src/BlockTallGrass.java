// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            BlockFlower, IBlockAccess, WorldChunkManager, ColorizerGrass, 
//            Item

public class BlockTallGrass extends BlockFlower
{

    protected BlockTallGrass(int i, int j)
    {
        super(i, j);
        float f = 0.4F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
    }

    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if(j == 1)
        {
            return blockIndexInTexture;
        }
        if(j == 2)
        {
            return blockIndexInTexture + 16 + 1;
        }
        if(j == 0)
        {
            return blockIndexInTexture + 16;
        } else
        {
            return blockIndexInTexture;
        }
    }

    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = iblockaccess.getBlockMetadata(i, j, k);
        if(l == 0)
        {
            return 0xffffff;
        } else
        {
            long l1 = i * 0x2fc20f + k * 0x5d8875 + j;
            l1 = l1 * l1 * 0x285b825L + l1 * 11L;
            i = (int)((long)i + (l1 >> 14 & 31L));
            j = (int)((long)j + (l1 >> 19 & 31L));
            k = (int)((long)k + (l1 >> 24 & 31L));
            iblockaccess.getWorldChunkManager().func_4069_a(i, k, 1, 1);
            double d = iblockaccess.getWorldChunkManager().temperature[0];
            double d1 = iblockaccess.getWorldChunkManager().humidity[0];
            return ColorizerGrass.getGrassColor(d, d1);
        }
    }

    public int idDropped(int i, Random random)
    {
        if(random.nextInt(8) == 0)
        {
            return Item.seeds.shiftedIndex;
        } else
        {
            return -1;
        }
    }
}
