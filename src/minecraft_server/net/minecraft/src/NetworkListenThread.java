// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

// Referenced classes of package net.minecraft.src:
//            NetworkAcceptThread, NetLoginHandler, NetworkManager, NetServerHandler

public class NetworkListenThread
{

    public NetworkListenThread(MinecraftServer minecraftserver, InetAddress inetaddress, int i)
        throws IOException
    {
        field_973_b = false;
        field_977_f = 0;
        pendingConnections = new ArrayList();
        playerList = new ArrayList();
        mcServer = minecraftserver;
        serverSocket = new ServerSocket(i, 0, inetaddress);
        serverSocket.setPerformancePreferences(0, 2, 1);
        field_973_b = true;
        networkAcceptThread = new NetworkAcceptThread(this, "Listen thread", minecraftserver);
        networkAcceptThread.start();
    }

    public void addPlayer(NetServerHandler netserverhandler)
    {
        playerList.add(netserverhandler);
    }

    private void addPendingConnection(NetLoginHandler netloginhandler)
    {
        if(netloginhandler == null)
        {
            throw new IllegalArgumentException("Got null pendingconnection!");
        } else
        {
            pendingConnections.add(netloginhandler);
            return;
        }
    }

    public void handleNetworkListenThread()
    {
        for(int i = 0; i < pendingConnections.size(); i++)
        {
            NetLoginHandler netloginhandler = (NetLoginHandler)pendingConnections.get(i);
            try
            {
                netloginhandler.tryLogin();
            }
            catch(Exception exception)
            {
                netloginhandler.kickUser("Internal server error");
                logger.log(Level.WARNING, (new StringBuilder()).append("Failed to handle packet: ").append(exception).toString(), exception);
            }
            if(netloginhandler.finishedProcessing)
            {
                pendingConnections.remove(i--);
            }
            netloginhandler.netManager.func_28138_a();
        }

        for(int j = 0; j < playerList.size(); j++)
        {
            NetServerHandler netserverhandler = (NetServerHandler)playerList.get(j);
            try
            {
                netserverhandler.handlePackets();
            }
            catch(Exception exception1)
            {
                logger.log(Level.WARNING, (new StringBuilder()).append("Failed to handle packet: ").append(exception1).toString(), exception1);
                netserverhandler.kickPlayer("Internal server error");
            }
            if(netserverhandler.connectionClosed)
            {
                playerList.remove(j--);
            }
            netserverhandler.netManager.func_28138_a();
        }

    }

    static ServerSocket func_713_a(NetworkListenThread networklistenthread)
    {
        return networklistenthread.serverSocket;
    }

    static int func_712_b(NetworkListenThread networklistenthread)
    {
        return networklistenthread.field_977_f++;
    }

    static void func_716_a(NetworkListenThread networklistenthread, NetLoginHandler netloginhandler)
    {
        networklistenthread.addPendingConnection(netloginhandler);
    }

    public static Logger logger = Logger.getLogger("Minecraft");
    private ServerSocket serverSocket;
    private Thread networkAcceptThread;
    public volatile boolean field_973_b;
    private int field_977_f;
    private ArrayList pendingConnections;
    private ArrayList playerList;
    public MinecraftServer mcServer;

}
