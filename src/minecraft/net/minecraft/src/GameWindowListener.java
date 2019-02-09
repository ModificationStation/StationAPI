// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import net.minecraft.client.Minecraft;

public final class GameWindowListener extends WindowAdapter
{

    public GameWindowListener(Minecraft minecraft, Thread thread)
    {
//        super();
        mc = minecraft;
        mcThread = thread;
    }

    public void windowClosing(WindowEvent windowevent)
    {
        mc.shutdown();
        try
        {
            mcThread.join();
        }
        catch(InterruptedException interruptedexception)
        {
            interruptedexception.printStackTrace();
        }
        System.exit(0);
    }

    final Minecraft mc; /* synthetic field */
    final Thread mcThread; /* synthetic field */
}
