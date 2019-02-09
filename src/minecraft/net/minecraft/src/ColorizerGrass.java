// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


public class ColorizerGrass
{

    public ColorizerGrass()
    {
    }

    public static void func_28181_a(int ai[])
    {
        grassBuffer = ai;
    }

    public static int getGrassColor(double d, double d1)
    {
        d1 *= d;
        int i = (int)((1.0D - d) * 255D);
        int j = (int)((1.0D - d1) * 255D);
        return grassBuffer[j << 8 | i];
    }

    private static int grassBuffer[] = new int[0x10000];

}
