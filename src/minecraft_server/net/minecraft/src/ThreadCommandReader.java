// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import net.minecraft.server.MinecraftServer;

public class ThreadCommandReader extends Thread
{

    public ThreadCommandReader(MinecraftServer minecraftserver)
    {
//        super();
        mcServer = minecraftserver;
    }

    public void run()
    {
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        try
        {
            while(!mcServer.serverStopped && MinecraftServer.isServerRunning(mcServer) && (s = bufferedreader.readLine()) != null) 
            {
                mcServer.addCommand(s, mcServer);
            }
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    final MinecraftServer mcServer; /* synthetic field */
}
