// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//            StatList, AchievementMap, StatTypeSimple, StatTypeTime, 
//            StatTypeDistance, IStatType

public class StatBase
{

    public StatBase(int i, String s, IStatType istattype)
    {
        field_27058_g = false;
        statId = i;
        statName = s;
        field_25065_a = istattype;
    }

    public StatBase(int i, String s)
    {
        this(i, s, field_27056_i);
    }

    public StatBase func_27052_e()
    {
        field_27058_g = true;
        return this;
    }

    public StatBase func_27053_d()
    {
        if(StatList.field_25104_C.containsKey(Integer.valueOf(statId)))
        {
            throw new RuntimeException((new StringBuilder()).append("Duplicate stat id: \"").append(((StatBase)StatList.field_25104_C.get(Integer.valueOf(statId))).statName).append("\" and \"").append(statName).append("\" at id ").append(statId).toString());
        } else
        {
            StatList.field_25123_a.add(this);
            StatList.field_25104_C.put(Integer.valueOf(statId), this);
            field_27057_h = AchievementMap.func_25132_a(statId);
            return this;
        }
    }

    public String toString()
    {
        return statName;
    }

    public final int statId;
    public final String statName;
    public boolean field_27058_g;
    public String field_27057_h;
    private final IStatType field_25065_a;
    private static NumberFormat field_25066_b;
    public static IStatType field_27056_i = new StatTypeSimple();
    private static DecimalFormat field_25068_c = new DecimalFormat("########0.00");
    public static IStatType field_27055_j = new StatTypeTime();
    public static IStatType field_27054_k = new StatTypeDistance();

    static 
    {
        field_25066_b = NumberFormat.getIntegerInstance(Locale.US);
    }
}
