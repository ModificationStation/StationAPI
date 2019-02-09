// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class ThreadSleepForever extends Thread
{

    public ThreadSleepForever(Minecraft minecraft, String s)
    {
        super(s);
        mc = minecraft;
        setDaemon(true);
        start();
    }

    public void run()
    {
        while(mc.running) 
        {
            try
            {
                Thread.sleep(0x7fffffffL);
            }
            catch(InterruptedException interruptedexception) { }
        }
    }

    final Minecraft mc; /* synthetic field */
}
