// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet38EntityStatus extends Packet
{

    public Packet38EntityStatus()
    {
    }

    public Packet38EntityStatus(int i, byte byte0)
    {
        entityId = i;
        entityStatus = byte0;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        entityId = datainputstream.readInt();
        entityStatus = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(entityId);
        dataoutputstream.writeByte(entityStatus);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_9001_a(this);
    }

    public int getPacketSize()
    {
        return 5;
    }

    public int entityId;
    public byte entityStatus;
}
