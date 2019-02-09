// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            NoiseGenerator, NoiseGenerator2

public class NoiseGeneratorOctaves2 extends NoiseGenerator
{

    public NoiseGeneratorOctaves2(Random random, int i)
    {
        field_4307_b = i;
        field_4308_a = new NoiseGenerator2[i];
        for(int j = 0; j < i; j++)
        {
            field_4308_a[j] = new NoiseGenerator2(random);
        }

    }

    public double[] func_4101_a(double ad[], double d, double d1, int i, int j, 
            double d2, double d3, double d4)
    {
        return func_4100_a(ad, d, d1, i, j, d2, d3, d4, 0.5D);
    }

    public double[] func_4100_a(double ad[], double d, double d1, int i, int j, 
            double d2, double d3, double d4, double d5)
    {
        d2 /= 1.5D;
        d3 /= 1.5D;
        if(ad == null || ad.length < i * j)
        {
            ad = new double[i * j];
        } else
        {
            for(int k = 0; k < ad.length; k++)
            {
                ad[k] = 0.0D;
            }

        }
        double d6 = 1.0D;
        double d7 = 1.0D;
        for(int l = 0; l < field_4307_b; l++)
        {
            field_4308_a[l].func_4115_a(ad, d, d1, i, j, d2 * d7, d3 * d7, 0.55000000000000004D / d6);
            d7 *= d4;
            d6 *= d5;
        }

        return ad;
    }

    private NoiseGenerator2 field_4308_a[];
    private int field_4307_b;
}
