// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet100OpenWindow extends Packet
{

    public Packet100OpenWindow()
    {
    }

    public Packet100OpenWindow(int i, int j, String s, int k)
    {
        windowId = i;
        inventoryType = j;
        windowTitle = s;
        slotsCount = k;
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_20004_a(this);
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        windowId = datainputstream.readByte();
        inventoryType = datainputstream.readByte();
        windowTitle = datainputstream.readUTF();
        slotsCount = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeByte(windowId);
        dataoutputstream.writeByte(inventoryType);
        dataoutputstream.writeUTF(windowTitle);
        dataoutputstream.writeByte(slotsCount);
    }

    public int getPacketSize()
    {
        return 3 + windowTitle.length();
    }

    public int windowId;
    public int inventoryType;
    public String windowTitle;
    public int slotsCount;
}
