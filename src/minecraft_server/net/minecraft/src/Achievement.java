// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;

// Referenced classes of package net.minecraft.src:
//            StatBase, ItemStack, StatCollector, AchievementList, 
//            Item, Block

public class Achievement extends StatBase
{

    public Achievement(int i, String s, int j, int k, Item item, Achievement achievement)
    {
        this(i, s, j, k, new ItemStack(item), achievement);
    }

    public Achievement(int i, String s, int j, int k, Block block, Achievement achievement)
    {
        this(i, s, j, k, new ItemStack(block), achievement);
    }

    public Achievement(int i, String s, int j, int k, ItemStack itemstack, Achievement achievement)
    {
        super(0x500000 + i, StatCollector.translateToLocal((new StringBuilder()).append("achievement.").append(s).toString()));
        theItemStack = itemstack;
        field_27063_l = StatCollector.translateToLocal((new StringBuilder()).append("achievement.").append(s).append(".desc").toString());
        field_25067_a = j;
        field_27991_b = k;
        if(j < AchievementList.field_27114_a)
        {
            AchievementList.field_27114_a = j;
        }
        if(k < AchievementList.field_27113_b)
        {
            AchievementList.field_27113_b = k;
        }
        if(j > AchievementList.field_27112_c)
        {
            AchievementList.field_27112_c = j;
        }
        if(k > AchievementList.field_27111_d)
        {
            AchievementList.field_27111_d = k;
        }
        field_27992_c = achievement;
    }

    public Achievement func_27059_a()
    {
        field_27058_g = true;
        return this;
    }

    public Achievement func_27060_b()
    {
        field_27062_m = true;
        return this;
    }

    public Achievement func_27061_c()
    {
        super.func_27053_d();
        AchievementList.field_25129_a.add(this);
        return this;
    }

    public StatBase func_27053_d()
    {
        return func_27061_c();
    }

    public StatBase func_27052_e()
    {
        return func_27059_a();
    }

    public final int field_25067_a;
    public final int field_27991_b;
    public final Achievement field_27992_c;
    private final String field_27063_l;
    public final ItemStack theItemStack;
    private boolean field_27062_m;
}
