// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.File;

// Referenced classes of package net.minecraft.src:
//            StatsSyncher

class ThreadStatSyncherReceive extends Thread
{

    ThreadStatSyncherReceive(StatsSyncher statssyncher)
    {
//        super();
        field_27231_a = statssyncher;
    }

    public void run()
    {
        try
        {
            if(StatsSyncher.func_27422_a(field_27231_a) != null)
            {
                StatsSyncher.func_27412_a(field_27231_a, StatsSyncher.func_27422_a(field_27231_a), StatsSyncher.func_27423_b(field_27231_a), StatsSyncher.func_27411_c(field_27231_a), StatsSyncher.func_27413_d(field_27231_a));
            } else
            if(StatsSyncher.func_27423_b(field_27231_a).exists())
            {
                StatsSyncher.func_27421_a(field_27231_a, StatsSyncher.func_27409_a(field_27231_a, StatsSyncher.func_27423_b(field_27231_a), StatsSyncher.func_27411_c(field_27231_a), StatsSyncher.func_27413_d(field_27231_a)));
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            StatsSyncher.func_27416_a(field_27231_a, false);
        }
    }

    final StatsSyncher field_27231_a; /* synthetic field */
}
