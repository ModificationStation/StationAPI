// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Comparator;

// Referenced classes of package net.minecraft.src:
//            StatCrafting, GuiSlotStatsBlock, StatList, GuiStats, 
//            StatFileWriter

class SorterStatsBlock
    implements Comparator
{

    SorterStatsBlock(GuiSlotStatsBlock guislotstatsblock, GuiStats guistats)
    {
//        super();
        field_27298_b = guislotstatsblock;
        field_27299_a = guistats;
    }

    public int func_27297_a(StatCrafting statcrafting, StatCrafting statcrafting1)
    {
        int i = statcrafting.func_25072_b();
        int j = statcrafting1.func_25072_b();
        StatBase statbase = null;
        StatBase statbase1 = null;
        if(field_27298_b.field_27271_e == 2)
        {
            statbase = StatList.mineBlockStatArray[i];
            statbase1 = StatList.mineBlockStatArray[j];
        } else
        if(field_27298_b.field_27271_e == 0)
        {
            statbase = StatList.field_25158_z[i];
            statbase1 = StatList.field_25158_z[j];
        } else
        if(field_27298_b.field_27271_e == 1)
        {
            statbase = StatList.field_25172_A[i];
            statbase1 = StatList.field_25172_A[j];
        }
        if(statbase != null || statbase1 != null)
        {
            if(statbase == null)
            {
                return 1;
            }
            if(statbase1 == null)
            {
                return -1;
            }
            int k = GuiStats.func_27142_c(field_27298_b.field_27274_a).writeStat(statbase);
            int l = GuiStats.func_27142_c(field_27298_b.field_27274_a).writeStat(statbase1);
            if(k != l)
            {
                return (k - l) * field_27298_b.field_27270_f;
            }
        }
        return i - j;
    }

    public int compare(Object obj, Object obj1)
    {
        return func_27297_a((StatCrafting)obj, (StatCrafting)obj1);
    }

    final GuiStats field_27299_a; /* synthetic field */
    final GuiSlotStatsBlock field_27298_b; /* synthetic field */
}
