// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet106Transaction extends Packet
{

    public Packet106Transaction()
    {
    }

    public Packet106Transaction(int i, short word0, boolean flag)
    {
        windowId = i;
        shortWindowId = word0;
        field_20035_c = flag;
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_20008_a(this);
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        windowId = datainputstream.readByte();
        shortWindowId = datainputstream.readShort();
        field_20035_c = datainputstream.readByte() != 0;
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeByte(windowId);
        dataoutputstream.writeShort(shortWindowId);
        dataoutputstream.writeByte(field_20035_c ? 1 : 0);
    }

    public int getPacketSize()
    {
        return 4;
    }

    public int windowId;
    public short shortWindowId;
    public boolean field_20035_c;
}
