// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import net.minecraft.server.MinecraftServer;

final class ServerWindowAdapter extends WindowAdapter
{

    ServerWindowAdapter(MinecraftServer minecraftserver)
    {
//        super();
        mcServer = minecraftserver;
    }

    public void windowClosing(WindowEvent windowevent)
    {
        mcServer.initiateShutdown();
        while(!mcServer.serverStopped) 
        {
            try
            {
                Thread.sleep(100L);
            }
            catch(InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }
        System.exit(0);
    }

    final MinecraftServer mcServer; /* synthetic field */
}
