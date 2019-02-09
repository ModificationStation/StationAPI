// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.PrintStream;

// Referenced classes of package net.minecraft.src:
//            World, Chunk, Block, EnumSkyBlock

public class MetadataChunkBlock
{

    public MetadataChunkBlock(EnumSkyBlock enumskyblock, int i, int j, int k, int l, int i1, int j1)
    {
        field_957_a = enumskyblock;
        field_956_b = i;
        field_962_c = j;
        field_961_d = k;
        field_960_e = l;
        field_959_f = i1;
        field_958_g = j1;
    }

    public void func_4107_a(World world)
    {
        int i = (field_960_e - field_956_b) + 1;
        int j = (field_959_f - field_962_c) + 1;
        int k = (field_958_g - field_961_d) + 1;
        int l = i * j * k;
        if(l > 32768)
        {
            System.out.println("Light too large, skipping!");
            return;
        }
        int i1 = 0;
        int j1 = 0;
        boolean flag = false;
        boolean flag1 = false;
        for(int k1 = field_956_b; k1 <= field_960_e; k1++)
        {
            for(int l1 = field_961_d; l1 <= field_958_g; l1++)
            {
                int i2 = k1 >> 4;
                int j2 = l1 >> 4;
                boolean flag2 = false;
                if(flag && i2 == i1 && j2 == j1)
                {
                    flag2 = flag1;
                } else
                {
                    flag2 = world.doChunksNearChunkExist(k1, 0, l1, 1);
                    if(flag2)
                    {
                        Chunk chunk = world.getChunkFromChunkCoords(k1 >> 4, l1 >> 4);
                        if(chunk.func_21101_g())
                        {
                            flag2 = false;
                        }
                    }
                    flag1 = flag2;
                    i1 = i2;
                    j1 = j2;
                }
                if(!flag2)
                {
                    continue;
                }
                if(field_962_c < 0)
                {
                    field_962_c = 0;
                }
                if(field_959_f >= 128)
                {
                    field_959_f = 127;
                }
                for(int k2 = field_962_c; k2 <= field_959_f; k2++)
                {
                    int l2 = world.getSavedLightValue(field_957_a, k1, k2, l1);
                    int i3 = 0;
                    int j3 = world.getBlockId(k1, k2, l1);
                    int k3 = Block.lightOpacity[j3];
                    if(k3 == 0)
                    {
                        k3 = 1;
                    }
                    int l3 = 0;
                    if(field_957_a == EnumSkyBlock.Sky)
                    {
                        if(world.canExistingBlockSeeTheSky(k1, k2, l1))
                        {
                            l3 = 15;
                        }
                    } else
                    if(field_957_a == EnumSkyBlock.Block)
                    {
                        l3 = Block.lightValue[j3];
                    }
                    if(k3 >= 15 && l3 == 0)
                    {
                        i3 = 0;
                    } else
                    {
                        int i4 = world.getSavedLightValue(field_957_a, k1 - 1, k2, l1);
                        int k4 = world.getSavedLightValue(field_957_a, k1 + 1, k2, l1);
                        int l4 = world.getSavedLightValue(field_957_a, k1, k2 - 1, l1);
                        int i5 = world.getSavedLightValue(field_957_a, k1, k2 + 1, l1);
                        int j5 = world.getSavedLightValue(field_957_a, k1, k2, l1 - 1);
                        int k5 = world.getSavedLightValue(field_957_a, k1, k2, l1 + 1);
                        i3 = i4;
                        if(k4 > i3)
                        {
                            i3 = k4;
                        }
                        if(l4 > i3)
                        {
                            i3 = l4;
                        }
                        if(i5 > i3)
                        {
                            i3 = i5;
                        }
                        if(j5 > i3)
                        {
                            i3 = j5;
                        }
                        if(k5 > i3)
                        {
                            i3 = k5;
                        }
                        i3 -= k3;
                        if(i3 < 0)
                        {
                            i3 = 0;
                        }
                        if(l3 > i3)
                        {
                            i3 = l3;
                        }
                    }
                    if(l2 == i3)
                    {
                        continue;
                    }
                    world.setLightValue(field_957_a, k1, k2, l1, i3);
                    int j4 = i3 - 1;
                    if(j4 < 0)
                    {
                        j4 = 0;
                    }
                    world.neighborLightPropagationChanged(field_957_a, k1 - 1, k2, l1, j4);
                    world.neighborLightPropagationChanged(field_957_a, k1, k2 - 1, l1, j4);
                    world.neighborLightPropagationChanged(field_957_a, k1, k2, l1 - 1, j4);
                    if(k1 + 1 >= field_960_e)
                    {
                        world.neighborLightPropagationChanged(field_957_a, k1 + 1, k2, l1, j4);
                    }
                    if(k2 + 1 >= field_959_f)
                    {
                        world.neighborLightPropagationChanged(field_957_a, k1, k2 + 1, l1, j4);
                    }
                    if(l1 + 1 >= field_958_g)
                    {
                        world.neighborLightPropagationChanged(field_957_a, k1, k2, l1 + 1, j4);
                    }
                }

            }

        }

    }

    public boolean func_692_a(int i, int j, int k, int l, int i1, int j1)
    {
        if(i >= field_956_b && j >= field_962_c && k >= field_961_d && l <= field_960_e && i1 <= field_959_f && j1 <= field_958_g)
        {
            return true;
        }
        int k1 = 1;
        if(i >= field_956_b - k1 && j >= field_962_c - k1 && k >= field_961_d - k1 && l <= field_960_e + k1 && i1 <= field_959_f + k1 && j1 <= field_958_g + k1)
        {
            int l1 = field_960_e - field_956_b;
            int i2 = field_959_f - field_962_c;
            int j2 = field_958_g - field_961_d;
            if(i > field_956_b)
            {
                i = field_956_b;
            }
            if(j > field_962_c)
            {
                j = field_962_c;
            }
            if(k > field_961_d)
            {
                k = field_961_d;
            }
            if(l < field_960_e)
            {
                l = field_960_e;
            }
            if(i1 < field_959_f)
            {
                i1 = field_959_f;
            }
            if(j1 < field_958_g)
            {
                j1 = field_958_g;
            }
            int k2 = l - i;
            int l2 = i1 - j;
            int i3 = j1 - k;
            int j3 = l1 * i2 * j2;
            int k3 = k2 * l2 * i3;
            if(k3 - j3 <= 2)
            {
                field_956_b = i;
                field_962_c = j;
                field_961_d = k;
                field_960_e = l;
                field_959_f = i1;
                field_958_g = j1;
                return true;
            }
        }
        return false;
    }

    public final EnumSkyBlock field_957_a;
    public int field_956_b;
    public int field_962_c;
    public int field_961_d;
    public int field_960_e;
    public int field_959_f;
    public int field_958_g;
}
