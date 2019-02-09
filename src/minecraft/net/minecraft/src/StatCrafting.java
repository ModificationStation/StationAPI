// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            StatBase

public class StatCrafting extends StatBase
{

    public StatCrafting(int i, String s, int j)
    {
        super(i, s);
        field_25073_a = j;
    }

    public int func_25072_b()
    {
        return field_25073_a;
    }

    private final int field_25073_a;
}
