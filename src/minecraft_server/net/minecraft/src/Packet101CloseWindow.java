// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet101CloseWindow extends Packet
{

    public Packet101CloseWindow()
    {
    }

    public Packet101CloseWindow(int i)
    {
        windowId = i;
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleCraftingGuiClosedPacked(this);
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        windowId = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeByte(windowId);
    }

    public int getPacketSize()
    {
        return 1;
    }

    public int windowId;
}
