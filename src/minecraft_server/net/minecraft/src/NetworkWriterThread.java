// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.DataOutputStream;
import java.io.IOException;

// Referenced classes of package net.minecraft.src:
//            NetworkManager

class NetworkWriterThread extends Thread
{

    NetworkWriterThread(NetworkManager networkmanager, String s)
    {
        super(s);
        netManager = networkmanager;
    }

    public void run()
    {
        synchronized(NetworkManager.threadSyncObject)
        {
            NetworkManager.numWriteThreads++;
        }
        try
        {
            do
            {
                if(!NetworkManager.isRunning(netManager))
                {
                    break;
                }
                while(NetworkManager.sendNetworkPacket(netManager)) ;
                try
                {
                    sleep(100L);
                }
                catch(InterruptedException interruptedexception) { }
                try
                {
                    if(NetworkManager.func_28136_f(netManager) != null)
                    {
                        NetworkManager.func_28136_f(netManager).flush();
                    }
                }
                catch(IOException ioexception)
                {
                    if(!NetworkManager.func_28135_e(netManager))
                    {
                        NetworkManager.func_30007_a(netManager, ioexception);
                    }
                    ioexception.printStackTrace();
                }
            } while(true);
        }
        finally
        {
            synchronized(NetworkManager.threadSyncObject)
            {
                NetworkManager.numWriteThreads--;
            }
        }
    }

    final NetworkManager netManager; /* synthetic field */
}
