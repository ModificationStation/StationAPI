// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            J_ThingWithPosition

final class J_PositionTrackingPushbackReader
    implements J_ThingWithPosition
{

    public J_PositionTrackingPushbackReader(Reader reader)
    {
        field_27337_b = 0;
        field_27340_c = 1;
        field_27339_d = false;
        field_27338_a = new PushbackReader(reader);
    }

    public void func_27334_a(char c)
        throws IOException
    {
        field_27337_b--;
        if(field_27337_b < 0)
        {
            field_27337_b = 0;
        }
        field_27338_a.unread(c);
    }

    public void func_27335_a(char ac[])
    {
        field_27337_b = field_27337_b - ac.length;
        if(field_27337_b < 0)
        {
            field_27337_b = 0;
        }
    }

    public int func_27333_c()
        throws IOException
    {
        int i = field_27338_a.read();
        func_27332_a(i);
        return i;
    }

    public int func_27336_b(char ac[])
        throws IOException
    {
        int i = field_27338_a.read(ac);
        char ac1[] = ac;
        int j = ac1.length;
        for(int k = 0; k < j; k++)
        {
            char c = ac1[k];
            func_27332_a(c);
        }

        return i;
    }

    private void func_27332_a(int i)
    {
        if(13 == i)
        {
            field_27337_b = 0;
            field_27340_c++;
            field_27339_d = true;
        } else
        {
            if(10 == i && !field_27339_d)
            {
                field_27337_b = 0;
                field_27340_c++;
            } else
            {
                field_27337_b++;
            }
            field_27339_d = false;
        }
    }

    public int func_27331_a()
    {
        return field_27337_b;
    }

    public int func_27330_b()
    {
        return field_27340_c;
    }

    private final PushbackReader field_27338_a;
    private int field_27337_b;
    private int field_27340_c;
    private boolean field_27339_d;
}
