// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.text.DecimalFormat;

// Referenced classes of package net.minecraft.src:
//            IStatType, StatBase

final class StatTypeDistance
    implements IStatType
{

    StatTypeDistance()
    {
    }

    public String func_27192_a(int i)
    {
        int j = i;
        double d = (double)j / 100D;
        double d1 = d / 1000D;
        if(d1 > 0.5D)
        {
            return (new StringBuilder()).append(StatBase.func_27081_j().format(d1)).append(" km").toString();
        }
        if(d > 0.5D)
        {
            return (new StringBuilder()).append(StatBase.func_27081_j().format(d)).append(" m").toString();
        } else
        {
            return (new StringBuilder()).append(i).append(" cm").toString();
        }
    }
}
