// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            GuiSlotStats, StatList, StatCrafting, GuiStats, 
//            StatFileWriter, SorterStatsItem, Tessellator

class GuiSlotStatsItem extends GuiSlotStats
{

    public GuiSlotStatsItem(GuiStats guistats)
    {
        super(guistats);
        field_27275_a = guistats;
        field_27273_c = new ArrayList();
        Iterator iterator = StatList.field_25186_c.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            StatCrafting statcrafting = (StatCrafting)iterator.next();
            boolean flag = false;
            int i = statcrafting.func_25072_b();
            if(GuiStats.func_27142_c(guistats).writeStat(statcrafting) > 0)
            {
                flag = true;
            } else
            if(StatList.field_25170_B[i] != null && GuiStats.func_27142_c(guistats).writeStat(StatList.field_25170_B[i]) > 0)
            {
                flag = true;
            } else
            if(StatList.field_25158_z[i] != null && GuiStats.func_27142_c(guistats).writeStat(StatList.field_25158_z[i]) > 0)
            {
                flag = true;
            }
            if(flag)
            {
                field_27273_c.add(statcrafting);
            }
        } while(true);
        field_27272_d = new SorterStatsItem(this, guistats);
    }

    protected void func_27260_a(int i, int j, Tessellator tessellator)
    {
        super.func_27260_a(i, j, tessellator);
        if(field_27268_b == 0)
        {
            GuiStats.func_27128_a(field_27275_a, ((i + 115) - 18) + 1, j + 1 + 1, 72, 18);
        } else
        {
            GuiStats.func_27128_a(field_27275_a, (i + 115) - 18, j + 1, 72, 18);
        }
        if(field_27268_b == 1)
        {
            GuiStats.func_27128_a(field_27275_a, ((i + 165) - 18) + 1, j + 1 + 1, 18, 18);
        } else
        {
            GuiStats.func_27128_a(field_27275_a, (i + 165) - 18, j + 1, 18, 18);
        }
        if(field_27268_b == 2)
        {
            GuiStats.func_27128_a(field_27275_a, ((i + 215) - 18) + 1, j + 1 + 1, 36, 18);
        } else
        {
            GuiStats.func_27128_a(field_27275_a, (i + 215) - 18, j + 1, 36, 18);
        }
    }

    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator)
    {
        StatCrafting statcrafting = func_27264_b(i);
        int i1 = statcrafting.func_25072_b();
        GuiStats.func_27148_a(field_27275_a, j + 40, k, i1);
        func_27265_a((StatCrafting)StatList.field_25170_B[i1], j + 115, k, i % 2 == 0);
        func_27265_a((StatCrafting)StatList.field_25158_z[i1], j + 165, k, i % 2 == 0);
        func_27265_a(statcrafting, j + 215, k, i % 2 == 0);
    }

    protected String func_27263_a(int i)
    {
        if(i == 1)
        {
            return "stat.crafted";
        }
        if(i == 2)
        {
            return "stat.used";
        } else
        {
            return "stat.depleted";
        }
    }

    final GuiStats field_27275_a; /* synthetic field */
}
