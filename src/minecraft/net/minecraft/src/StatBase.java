// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//            StatList, AchievementMap, IStatType, StatTypeSimple, 
//            StatTypeTime, StatTypeDistance

public class StatBase
{

    public StatBase(int i, String s, IStatType istattype)
    {
        field_27088_g = false;
        statId = i;
        statName = s;
        field_26902_a = istattype;
    }

    public StatBase(int i, String s)
    {
        this(i, s, field_27087_i);
    }

    public StatBase func_27082_h()
    {
        field_27088_g = true;
        return this;
    }

    public StatBase registerStat()
    {
        if(StatList.field_25169_C.containsKey(Integer.valueOf(statId)))
        {
            throw new RuntimeException((new StringBuilder()).append("Duplicate stat id: \"").append(((StatBase)StatList.field_25169_C.get(Integer.valueOf(statId))).statName).append("\" and \"").append(statName).append("\" at id ").append(statId).toString());
        } else
        {
            StatList.field_25188_a.add(this);
            StatList.field_25169_C.put(Integer.valueOf(statId), this);
            statGuid = AchievementMap.getGuid(statId);
            return this;
        }
    }

    public boolean func_25067_a()
    {
        return false;
    }

    public String func_27084_a(int i)
    {
        return field_26902_a.func_27192_a(i);
    }

    public String toString()
    {
        return statName;
    }

    static NumberFormat func_27083_i()
    {
        return field_26903_b;
    }

    static DecimalFormat func_27081_j()
    {
        return field_26904_c;
    }

    public final int statId;
    public final String statName;
    public boolean field_27088_g;
    public String statGuid;
    private final IStatType field_26902_a;
    private static NumberFormat field_26903_b;
    public static IStatType field_27087_i = new StatTypeSimple();
    private static DecimalFormat field_26904_c = new DecimalFormat("########0.00");
    public static IStatType field_27086_j = new StatTypeTime();
    public static IStatType field_27085_k = new StatTypeDistance();

    static 
    {
        field_26903_b = NumberFormat.getIntegerInstance(Locale.US);
    }
}
