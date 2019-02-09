// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;

// Referenced classes of package net.minecraft.src:
//            StatBase, StatList, IStatType

public class StatBasic extends StatBase
{

    public StatBasic(int i, String s, IStatType istattype)
    {
        super(i, s, istattype);
    }

    public StatBasic(int i, String s)
    {
        super(i, s);
    }

    public StatBase registerStat()
    {
        super.registerStat();
        StatList.field_25187_b.add(this);
        return this;
    }
}
