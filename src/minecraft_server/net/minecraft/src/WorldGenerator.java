// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            World

public abstract class WorldGenerator
{

    public WorldGenerator()
    {
    }

    public abstract boolean generate(World world, Random random, int i, int j, int k);

    public void func_420_a(double d, double d1, double d2)
    {
    }
}
